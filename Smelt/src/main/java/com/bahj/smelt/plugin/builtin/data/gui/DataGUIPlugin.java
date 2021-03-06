package com.bahj.smelt.plugin.builtin.data.gui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.event.SmeltApplicationConfigurationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationSpecificationLoadedEvent;
import com.bahj.smelt.event.SmeltApplicationSpecificationUnloadedEvent;
import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.plugin.SmeltPluginDeclarationHandlerContext;
import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltBasicMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUICloseRequestedEvent;
import com.bahj.smelt.plugin.builtin.basegui.event.BaseGUIInitializingEvent;
import com.bahj.smelt.plugin.builtin.basegui.execution.GUIExecutionContext;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;
import com.bahj.smelt.plugin.builtin.data.model.database.event.DatabaseEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseOpenedEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseSavedEvent;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;
import com.bahj.smelt.util.swing.FileFilterUtils;

public class DataGUIPlugin implements SmeltPlugin {
    /** Tracks whether there are changes to the database which have not yet been saved. */
    private boolean dirtyDatabase = false;

    @Override
    public void registeredToApplicationModel(SmeltApplicationModel model) {
        // dataModelPlugin.addListener(new TypedEventListener<>(
        // Make sure that we add components to the GUI after the configuration is loaded.
        model.addListener(new TypedEventListener<>(SmeltApplicationConfigurationLoadedEvent.class,
                new EventListener<SmeltApplicationConfigurationLoadedEvent>() {
                    @Override
                    public void eventOccurred(SmeltApplicationConfigurationLoadedEvent event) {
                        // Make sure we add the appropriate content to the Smelt GUI. This consists of the menu
                        // items necessary to activate the opening and closing of databases.
                        SmeltApplicationModel model = event.getApplicationModel();
                        BaseGUIPlugin guiPlugin = model.getPluginRegistry().getPlugin(BaseGUIPlugin.class);
                        guiPlugin.addListener(new TypedEventListener<>(BaseGUIInitializingEvent.class,
                                new EventListener<BaseGUIInitializingEvent>() {
                            @Override
                            public void eventOccurred(BaseGUIInitializingEvent event) {
                                final DataModelPlugin dataModelPlugin = model.getPluginRegistry()
                                        .getPlugin(DataModelPlugin.class);
                                final BaseGUIPlugin baseGUIPlugin = model.getPluginRegistry()
                                        .getPlugin(BaseGUIPlugin.class);

                                MenuActions menuActions = new MenuActions(dataModelPlugin, baseGUIPlugin);

                                final Action newDatabaseAction = event.getContext()
                                        .constructExecutionAction(menuActions::newDatabase);
                                final Action openDatabaseAction = event.getContext()
                                        .constructExecutionAction(menuActions::openDatabase);
                                final Action saveDatabaseAction = event.getContext()
                                        .constructExecutionAction(menuActions::saveDatabase);
                                final Action saveDatabaseAsAction = event.getContext()
                                        .constructExecutionAction(menuActions::saveDatabaseAs);
                                final Action closeDatabaseAction = event.getContext()
                                        .constructExecutionAction(menuActions::closeDatabase);

                                newDatabaseAction.setEnabled(false);
                                openDatabaseAction.setEnabled(false);
                                saveDatabaseAction.setEnabled(false);
                                saveDatabaseAsAction.setEnabled(false);
                                closeDatabaseAction.setEnabled(false);

                                event.getContext()
                                        .addMenuItemGroup("File", new SmeltBasicMenuItem("New Database",
                                                newDatabaseAction, KeyEvent.VK_N),
                                        new SmeltBasicMenuItem("Open Database", openDatabaseAction, KeyEvent.VK_O),
                                        new SmeltBasicMenuItem("Save Database", saveDatabaseAction, KeyEvent.VK_S),
                                        new SmeltBasicMenuItem("Save Database As...", saveDatabaseAsAction,
                                                KeyEvent.VK_A),
                                        new SmeltBasicMenuItem("Close Database", closeDatabaseAction, KeyEvent.VK_C));
                                event.getContext().addMenuMnemonicSuggestion("File", KeyEvent.VK_F);

                                // When the specification is loaded, databases can be opened.
                                model.addListener(
                                        new TypedEventListener<>(SmeltApplicationSpecificationLoadedEvent.class,
                                                new EventListener<SmeltApplicationSpecificationLoadedEvent>() {
                                    @Override
                                    public void eventOccurred(SmeltApplicationSpecificationLoadedEvent event) {
                                        newDatabaseAction.setEnabled(true);
                                        openDatabaseAction.setEnabled(true);
                                    }
                                }));

                                // When a database is opened, it can be saved or closed.
                                dataModelPlugin.addListener(new TypedEventListener<>(DatabaseOpenedEvent.class,
                                        new EventListener<DatabaseOpenedEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseOpenedEvent event) {
                                        saveDatabaseAction.setEnabled(true);
                                        saveDatabaseAsAction.setEnabled(true);
                                        closeDatabaseAction.setEnabled(true);
                                    }
                                }));

                                // When the database is closed, it can no longer be saved or closed.
                                dataModelPlugin.addListener(new TypedEventListener<>(DatabaseClosedEvent.class,
                                        new EventListener<DatabaseClosedEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseClosedEvent event) {
                                        saveDatabaseAction.setEnabled(false);
                                        saveDatabaseAsAction.setEnabled(false);
                                        closeDatabaseAction.setEnabled(false);
                                    }
                                }));

                                // When the specification is unloaded, databases can no longer be opened.
                                model.addListener(
                                        new TypedEventListener<>(SmeltApplicationSpecificationUnloadedEvent.class,
                                                new EventListener<SmeltApplicationSpecificationUnloadedEvent>() {
                                    @Override
                                    public void eventOccurred(SmeltApplicationSpecificationUnloadedEvent event) {
                                        newDatabaseAction.setEnabled(false);
                                        openDatabaseAction.setEnabled(false);
                                    }
                                }));

                                // Keep the dirtyDatabase field up to date.
                                EventListener<DatabaseEvent> databaseEventListener = new EventListener<DatabaseEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseEvent event) {
                                        dirtyDatabase = true;
                                    }
                                };
                                dataModelPlugin.addListener(new TypedEventListener<>(DatabaseOpenedEvent.class,
                                        new EventListener<DatabaseOpenedEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseOpenedEvent event) {
                                        dirtyDatabase = false;
                                        event.getDatabase().addListener(databaseEventListener);
                                    }
                                }));
                                dataModelPlugin.addListener(new TypedEventListener<>(DatabaseSavedEvent.class,
                                        new EventListener<DatabaseSavedEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseSavedEvent event) {
                                        dirtyDatabase = false;
                                    }
                                }));
                                dataModelPlugin.addListener(new TypedEventListener<>(DatabaseClosedEvent.class,
                                        new EventListener<DatabaseClosedEvent>() {
                                    @Override
                                    public void eventOccurred(DatabaseClosedEvent event) {
                                        dirtyDatabase = false;
                                        event.getDatabase().removeListener(databaseEventListener);
                                    }
                                }));

                                // If the application is asked to close when the database hasn't yet been saved, we
                                // should prompt the user.
                                guiPlugin.addListener(new TypedEventListener<>(BaseGUICloseRequestedEvent.class,
                                        new EventListener<BaseGUICloseRequestedEvent>() {
                                    @Override
                                    public void eventOccurred(BaseGUICloseRequestedEvent event) {
                                        if (dirtyDatabase) {
                                            event.addChallenge((GUIExecutionContext context) -> {
                                                int answer = JOptionPane.showConfirmDialog(baseGUIPlugin.getBaseFrame(),
                                                        "You have not yet saved your changes.  Save now?",
                                                        "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION,
                                                        JOptionPane.WARNING_MESSAGE);
                                                switch (answer) {
                                                    case JOptionPane.YES_OPTION:
                                                        menuActions.saveDatabase(context);
                                                        return true;
                                                    case JOptionPane.NO_OPTION:
                                                        return true;
                                                    case JOptionPane.CANCEL_OPTION:
                                                        return false;
                                                    default:
                                                        // This would happen if the user closed the dialog. Let's be
                                                        // safe and assume they meant "cancel".
                                                        return false;
                                                }
                                            });
                                        }
                                    }
                                }));
                            }
                        }));
                    }
                }));

    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getDeclarationDependencyTypes() {
        // While the data declaration plugin does depend on the GUI plugin for GUI layout, it does not have any
        // dependency in terms of declaration handling order.
        return Collections.emptySet();
    }

    @Override
    public Set<Class<? extends SmeltPlugin>> getRuntimeDependencyTypes() {
        return new HashSet<>(Arrays.asList(DataModelPlugin.class, BaseGUIPlugin.class));
    }

    @Override
    public boolean claimsDeclaration(SmeltPluginDeclarationHandlerContext context, DeclarationNode declarationNode) {
        return false;
    }

    @Override
    public void processDeclarations(SmeltPluginDeclarationHandlerContext context, Set<DeclarationNode> declarationNodes)
            throws DeclarationProcessingException {

    }

    private static class MenuActions {
        private DataModelPlugin dataModelPlugin;
        private BaseGUIPlugin baseGUIPlugin;

        private File currentFile;

        public MenuActions(DataModelPlugin dataModelPlugin, BaseGUIPlugin baseGUIPlugin) {
            super();
            this.dataModelPlugin = dataModelPlugin;
            this.baseGUIPlugin = baseGUIPlugin;

            this.currentFile = null;
        }

        public void newDatabase(GUIExecutionContext context) {
            this.currentFile = null;
            // TODO: appropriate confirmation prompts if a database is already loaded
            dataModelPlugin.setDatabase(new SmeltDatabase());
        }

        public void openDatabase(GUIExecutionContext context) {
            // TODO: appropriate confirmation prompts if a database is already loaded
            JFileChooser chooser = new JFileChooser(); // TODO: based on directory of known filename or last opened file
            chooser.setFileFilter(FileFilterUtils.SMELT_DB_FILTER);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = chooser.showOpenDialog(this.baseGUIPlugin.getBaseFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                this.currentFile = null;
                try {
                    File selectedFile = chooser.getSelectedFile();
                    dataModelPlugin.openDatabase(selectedFile);
                    this.currentFile = selectedFile;
                } catch (IOException e) {
                    throw new NotYetImplementedException(e);
                } catch (DeserializationException e) {
                    throw new NotYetImplementedException(e);
                }
            }
        }

        public void saveDatabase(GUIExecutionContext context) {
            if (this.currentFile == null) {
                saveDatabaseAs(context);
            } else {
                try {
                    dataModelPlugin.saveDatabase(this.currentFile);
                } catch (IOException e) {
                    throw new NotYetImplementedException(e);
                } catch (SerializationException e) {
                    throw new NotYetImplementedException(e);
                }
            }
        }

        public void saveDatabaseAs(GUIExecutionContext context) {
            JFileChooser chooser = new JFileChooser(); // TODO: based on directory of known filename or last opened file
            chooser.setFileFilter(FileFilterUtils.SMELT_DB_FILTER);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = chooser.showSaveDialog(this.baseGUIPlugin.getBaseFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = chooser.getSelectedFile();
                    dataModelPlugin.saveDatabase(selectedFile);
                    this.currentFile = selectedFile;
                } catch (IOException e) {
                    throw new NotYetImplementedException(e);
                } catch (SerializationException e) {
                    throw new NotYetImplementedException(e);
                }
            }
        }

        public void closeDatabase(GUIExecutionContext context) {
            // TODO: appropriate confirmation prompts
            dataModelPlugin.setDatabase(null);
            this.currentFile = null;
        }
    }
}
