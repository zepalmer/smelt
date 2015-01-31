package com.bahj.smelt.plugin.builtin.editor.views.treebytype;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;

import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;

/**
 * A panel which displays a view of the current database by type.
 * @author Zachary Palmer
 */
public class DatabaseTreeViewByTypePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public DatabaseTreeViewByTypePanel(DataModelPlugin dataModelPlugin) {
        DatabaseTreeModelManager modelManager = new DatabaseTreeModelManager(dataModelPlugin);
        JTree tree = new JTree(modelManager.getTreeModel());
        
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        JScrollPane scrollPane = new JScrollPane(tree);
        
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        
        // TODO: renderer for the tree nodes that displays meaningful information
        
        // TODO: events when the tree's elements are selected
    }
}
