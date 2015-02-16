package com.bahj.smelt.plugin.builtin.viewbytype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.database.event.DatabaseObjectAddedEvent;
import com.bahj.smelt.plugin.builtin.data.model.database.event.DatabaseObjectRemovedEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseOpenedEvent;
import com.bahj.smelt.plugin.builtin.data.model.type.EnumType;
import com.bahj.smelt.plugin.builtin.data.model.type.PrimitiveType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueDescriptionUpdateEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.util.MappedComparator;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

/**
 * An object which creates a tree model based on the contents of a data model plugin. As the contents of that plugin
 * change, this object updates the tree model it created.
 * 
 * @author Zachary Palmer
 */
public class DatabaseTreeModelManager {
    private static final Comparator<SmeltType<?, ?>> SMELT_TYPE_COMPARATOR = new MappedComparator<SmeltType<?, ?>, String>(
            SmeltType::getName, Comparator.naturalOrder());
    private static final Comparator<SmeltValue<?, ?>> SMELT_VALUE_COMPARATOR = new MappedComparator<SmeltValue<?, ?>, String>(
            SmeltValue::getDescription, Comparator.naturalOrder());

    private TitleUpdateListener titleUpdateListener;

    private DataModelPlugin plugin;
    private DefaultTreeModel treeModel;

    public DatabaseTreeModelManager(DataModelPlugin plugin) {
        super();
        this.plugin = plugin;
        this.treeModel = new DefaultTreeModel(null, true);
        this.titleUpdateListener = new TitleUpdateListener();

        this.plugin.addListener(new TypedEventListener<>(DatabaseOpenedEvent.class,
                new EventListener<DatabaseOpenedEvent>() {
                    @Override
                    public void eventOccurred(DatabaseOpenedEvent event) {
                        treeModel.setRoot(buildRootNode());
                    }
                }));
        this.plugin.addListener(new TypedEventListener<>(DatabaseClosedEvent.class,
                new EventListener<DatabaseClosedEvent>() {
                    @Override
                    public void eventOccurred(DatabaseClosedEvent event) {
                        treeModel.setRoot(null);
                    }
                }));
        this.plugin.addListener(new TypedEventListener<>(DatabaseObjectAddedEvent.class,
                new EventListener<DatabaseObjectAddedEvent>() {
                    @Override
                    public void eventOccurred(DatabaseObjectAddedEvent event) {
                        handleValue(event.getWrapper());
                    }

                    // Using the following function to name V and E.  Eclipse is smart enough to infer this, but the
                    // Java 1.8.0_20 compiler is not.
                    private <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> void handleValue(
                            SmeltValueWrapper<V, E> valueWrapper) {
                        V value = valueWrapper.getSmeltValue();
                        DefaultMutableTreeNode valueNode = buildValueNode(value);
                        insertValueIntoTree(valueNode);
                        value.addListener(DatabaseTreeModelManager.this.titleUpdateListener);
                    }
                }));
        this.plugin.addListener(new TypedEventListener<>(DatabaseObjectRemovedEvent.class,
                new EventListener<DatabaseObjectRemovedEvent>() {
                    @Override
                    public void eventOccurred(DatabaseObjectRemovedEvent event) {
                        event.getWrapper().getSmeltValue()
                                .removeListener(DatabaseTreeModelManager.this.titleUpdateListener);
                        removeValueFromTree(event.getWrapper().getSmeltValue());
                    }
                }));
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    /**
     * Creates a fresh tree based on the contents of the data model plugin.
     * 
     * @return A new root node.
     */
    private DefaultMutableTreeNode buildRootNode() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        // The contents of the root node are the types of the data model.
        node.setAllowsChildren(true);
        Collection<SmeltType<?, ?>> types = this.plugin.getModel().getTypes().values();
        // Remove types which cannot be edited.
        List<SmeltType<?, ?>> editableTypes = types.stream()
                .filter((SmeltType<?, ?> type) -> (!(type instanceof PrimitiveType) && !(type instanceof EnumType)))
                .collect(Collectors.toList());
        // Let's sort these types in a nice order.
        editableTypes.sort(SMELT_TYPE_COMPARATOR);
        // Now create a node for each of them and add it to the root node.
        for (SmeltType<?, ?> type : editableTypes) {
            node.insert(buildTypeNode(type), node.getChildCount());
        }
        return node;
    }

    /**
     * Creates a fresh type node based on the contents of the data model plugin.
     * 
     * @param type
     *            The type for which to create a node.
     * @return A node representing the type.
     */
    private <T extends SmeltType<V, E>, V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> DefaultMutableTreeNode buildTypeNode(
            T type) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeTypeObject<T, V, E>(type));
        // The contents of the type node are the values of this type in the database.
        node.setAllowsChildren(true);
        List<V> values = new ArrayList<V>(this.plugin.getDatabase().getAllOfType(type));
        // Sort these values in some nice order.
        values.sort(SMELT_VALUE_COMPARATOR);
        // Add each value node to the type node.
        for (V value : values) {
            node.insert(buildValueNode(value), node.getChildCount());
        }
        return node;
    }

    /**
     * Creates a fresh value node based on the contents of the data model plugin.
     * 
     * @param The
     *            value for which to create a node.
     * @return A node representing the value.
     */
    private <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> DefaultMutableTreeNode buildValueNode(V value) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeValueObject<V, E>(value));
        // The value node has no children.
        node.setAllowsChildren(false);
        return node;
    }

    /**
     * Finds the tree node for the provided type.
     * 
     * @param type
     *            The type for which to find a node.
     * @return The node in question.
     */
    public DefaultMutableTreeNode getTypeNode(SmeltType<?, ?> type) {
        if (this.getTreeModel().getRoot() == null) {
            return null;
        }
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getTreeModel().getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            SmeltType<?, ?> childType = ((TreeTypeObject<?, ?, ?>) child.getUserObject()).getType();
            if (childType.equals(type)) {
                return (DefaultMutableTreeNode) root.getChildAt(i);
            }
        }
        return null;
    }

    /**
     * Finds a tree node for the provided value. This method does not depend upon the sorted order of the nodes and so
     * is safe to use even if the description of the node has changed.
     * 
     * @param value
     *            The value for which to find a node.
     * @return The node in question.
     */
    public DefaultMutableTreeNode getValueNode(SmeltValue<?, ?> value) {
        DefaultMutableTreeNode typeNode = getTypeNode(value.getType());
        if (typeNode == null) {
            return null;
        }
        for (int i = 0; i < typeNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) typeNode.getChildAt(i);
            SmeltValue<?, ?> childValue = ((TreeValueObject<?, ?>) child.getUserObject()).getValue();
            if (childValue.equals(value)) {
                return (DefaultMutableTreeNode) typeNode.getChildAt(i);
            }
        }
        return null;
    }

    /**
     * Inserts a value node into the tree.
     * 
     * @param valueNode
     *            The value node to insert.
     */
    public void insertValueIntoTree(DefaultMutableTreeNode valueNode) {
        // Search for the position at which to insert the value.
        SmeltValue<?, ?> value = ((TreeValueObject<?, ?>) valueNode.getUserObject()).getValue();
        DefaultMutableTreeNode typeNode = getTypeNode(value.getType());
        int index;
        for (index = 0; index < typeNode.getChildCount(); index++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) typeNode.getChildAt(index);
            SmeltValue<?, ?> childValue = ((TreeValueObject<?, ?>) child.getUserObject()).getValue();
            // If the value at this position is greater, then we've found our location.
            if (SMELT_VALUE_COMPARATOR.compare(childValue, value) > 0) {
                break;
            }
        }
        treeModel.insertNodeInto(valueNode, typeNode, index);
    }

    /**
     * Removes a value from the tree. This method does not depend upon the sorted order of nodes in the tree and so is
     * safe to call even if the description of the node has changed.
     * 
     * @param value
     *            The value to remove.
     * @return The removed node, or <code>null</code> if it is not found.
     */
    public DefaultMutableTreeNode removeValueFromTree(SmeltValue<?, ?> value) {
        DefaultMutableTreeNode typeNode = getTypeNode(value.getType());
        if (typeNode == null) {
            return null;
        }
        for (int index = 0; index < typeNode.getChildCount(); index++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) typeNode.getChildAt(index);
            if (((TreeValueObject<?, ?>) child.getUserObject()).getValue().equals(value)) {
                treeModel.removeNodeFromParent(child);
                return child;
            }
        }
        return null;
    }

    private void updateTitleForValueInTree(SmeltValue<?, ?> value) {
        // Find the type node which should host this value.
        DefaultMutableTreeNode typeNode = getTypeNode(value.getType());

        // Locate the value in that node's children.
        int childIndex = -1;
        DefaultMutableTreeNode valueNode = null;
        for (int i = 0; i < typeNode.getChildCount(); i++) {
            DefaultMutableTreeNode candidateNode = (DefaultMutableTreeNode) typeNode.getChildAt(i);
            if (((TreeValueObject<?, ?>) candidateNode.getUserObject()).getValue().equals(value)) {
                childIndex = i;
                valueNode = candidateNode;
            }
        }
        if (childIndex == -1) {
            throw new IllegalStateException("Updating title for value in tree but node not found! " + value);
        }

        // Determine whether the node is now out of place.
        boolean nodeBeforeShouldBeAfter = childIndex > 0
                && SMELT_VALUE_COMPARATOR.compare(value, ((TreeValueObject<?, ?>) ((DefaultMutableTreeNode) typeNode
                        .getChildAt(childIndex - 1)).getUserObject()).getValue()) < 0;
        boolean nodeAfterShouldBeBefore = childIndex < typeNode.getChildCount() - 1
                && SMELT_VALUE_COMPARATOR.compare(value, ((TreeValueObject<?, ?>) ((DefaultMutableTreeNode) typeNode
                        .getChildAt(childIndex + 1)).getUserObject()).getValue()) > 0;
        if (nodeBeforeShouldBeAfter || nodeAfterShouldBeBefore) {
            // The position of the node should change.
            insertValueIntoTree(removeValueFromTree(value));
        } else {
            // The node's position is fine; just the display needs to be updated.
            this.treeModel.nodeChanged(valueNode);
        }
    }

    private class TitleUpdateListener implements EventListener<SmeltValueEvent<?, ?>> {
        @Override
        public void eventOccurred(SmeltValueEvent<?, ?> event) {
            if (event instanceof SmeltValueDescriptionUpdateEvent) {
                updateTitleForValueInTree(event.getValue());
            }
        }
    }
}
