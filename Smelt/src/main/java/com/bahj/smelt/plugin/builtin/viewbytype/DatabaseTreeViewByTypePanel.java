package com.bahj.smelt.plugin.builtin.viewbytype;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.plugin.builtin.editor.EditorModel;
import com.bahj.smelt.util.NotYetImplementedException;

/**
 * A panel which displays a view of the current database by type.
 * 
 * @author Zachary Palmer
 */
public class DatabaseTreeViewByTypePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    /** The data model plugin which contains the data for this panel. */
    private DataModelPlugin plugin;
    /** The editor context for this panel. */
    private EditorModel editorModel;
    /** The tree displayed by this panel. */
    private JTree tree;

    public DatabaseTreeViewByTypePanel(DataModelPlugin dataModelPlugin, EditorModel editorModel) {
        this.plugin = dataModelPlugin;
        this.editorModel = editorModel;
        DatabaseTreeModelManager modelManager = new DatabaseTreeModelManager(dataModelPlugin);
        tree = new JTree(modelManager.getTreeModel());

        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        JScrollPane scrollPane = new JScrollPane(tree);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode node = getTreeNodeAt(e.getPoint());
                if (node != null && e.getClickCount() == 2 && (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                    doubleClickOn(node);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                considerPopupClick(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                considerPopupClick(e);
            }

            private void considerPopupClick(MouseEvent e) {
                DefaultMutableTreeNode node = getTreeNodeAt(e.getPoint());
                if (node != null && e.isPopupTrigger()) {
                    popupMenuOn(node, e.getPoint());
                }
            }
        });
    }

    private DefaultMutableTreeNode getTreeNodeAt(Point point) {
        int selRow = tree.getRowForLocation((int) point.getX(), (int) point.getY());
        TreePath selPath = tree.getPathForLocation((int) point.getX(), (int) point.getY());
        if (selRow != -1) {
            return (DefaultMutableTreeNode) selPath.getLastPathComponent();
        } else {
            return null;
        }
    }

    private void popupMenuOn(DefaultMutableTreeNode node, Point point) {
        TreeObject obj = (TreeObject) node.getUserObject();
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLabel(obj.toString());
        obj.visit(new TreeObjectVisitor<JPopupMenu, Void, RuntimeException>() {
            @Override
            public Void visitType(TreeTypeObject<?, ?, ?> obj, JPopupMenu popupMenu) {
                JMenuItem createItem = new JMenuItem("New");
                createItem.setMnemonic(KeyEvent.VK_N);
                createItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Create a new instance of this type and add it to the database.
                        if (plugin.getDatabase() != null) {
                            addTypeToDatabase(plugin.getDatabase(), obj.getType());
                        }
                    }

                    // We extract this expression to its own method to give names to V and E.  This is not necessary
                    // for Eclipse, but it's necessary for jdk1.8.0_20.
                    private <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> void addTypeToDatabase(
                            SmeltDatabase database, SmeltType<V, E> type) {
                        database.add(type.instantiate());
                    }
                });

                popupMenu.add(createItem);

                return null;
            }

            @Override
            public Void visitValue(TreeValueObject<?, ?> obj, JPopupMenu popupMenu) {
                // TODO: options for edit, delete, etc.
                throw new NotYetImplementedException();
            }
        }, popupMenu);

        popupMenu.show(tree, (int) point.getX(), (int) point.getY());
    }

    private void doubleClickOn(DefaultMutableTreeNode node) {
        TreeObject obj = (TreeObject) node.getUserObject();
        obj.visit(new TreeObjectVisitor<Void, Void, RuntimeException>() {
            @Override
            public Void visitType(TreeTypeObject<?, ?, ?> obj, Void arg) throws RuntimeException {
                // TODO: does anything belong here?
                return null;
            }

            @Override
            public Void visitValue(TreeValueObject<?, ?> obj, Void arg) throws RuntimeException {
                editorModel.openEditor(obj.getValue());
                return null;
            }
        }, null);
    }
}
