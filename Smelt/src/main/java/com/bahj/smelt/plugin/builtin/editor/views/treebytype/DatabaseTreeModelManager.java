package com.bahj.smelt.plugin.builtin.editor.views.treebytype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseOpenedEvent;
import com.bahj.smelt.plugin.builtin.data.model.type.EnumType;
import com.bahj.smelt.plugin.builtin.data.model.type.PrimitiveType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
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
    private DataModelPlugin plugin;
    private DefaultTreeModel treeModel;

    public DatabaseTreeModelManager(DataModelPlugin plugin) {
        super();
        this.plugin = plugin;
        this.treeModel = new DefaultTreeModel(null);

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
        // TODO: listeners that will update the tree model incrementally
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    /**
     * Creates a fresh tree based on the contents of the data model plugin.
     * 
     * @return A new root node.
     */
    private MutableTreeNode buildRootNode() {
        MutableTreeNode node = new DefaultMutableTreeNode();
        // The contents of the root node are the types of the data model.
        Collection<SmeltType<?>> types = this.plugin.getModel().getTypes().values();
        // Remove types which cannot be edited.
        List<SmeltType<?>> editableTypes = types.stream()
                .filter((SmeltType<?> type) -> (!(type instanceof PrimitiveType) && !(type instanceof EnumType)))
                .collect(Collectors.toList());
        // Let's sort these types in a nice order.
        editableTypes.sort(new MappedComparator<SmeltType<?>, String>(SmeltType::getName, Comparator.naturalOrder()));
        // Now create a node for each of them and add it to the root node.
        for (SmeltType<?> type : editableTypes) {
            node.insert(buildTypeNode(type), node.getChildCount());
        }
        return node;
    }
    
    /**
     * Creates a fresh type node based on the contents of the data model plugin.
     * 
     * @param type The type for which to create a node.
     * @return A node representing the type.
     */
   private <T extends SmeltType<V>, V extends SmeltValue<V>> MutableTreeNode buildTypeNode(T type) {
       MutableTreeNode node = new DefaultMutableTreeNode(type);
       // The contents of the type node are the values of this type in the database.
       List<V> values = new ArrayList<V>(this.plugin.getDatabase().getAllOfType(type));
       // Sort these values in some nice order.
       // TODO
       // Add each value node to the type node.
       for (V value : values) {
           node.insert(buildValueNode(value), node.getChildCount());
       }
       return node;
   }
   
   /**
    * Creates a fresh value node based on the contents of the data model plugin.
    * 
    * @param The value for which to create a node.
    * @return A node representing the value.
    */
   private <V extends SmeltValue<V>> MutableTreeNode buildValueNode(V value) {
       MutableTreeNode node = new DefaultMutableTreeNode(value);
       // The value node has no children.
       return node;
   }
}
