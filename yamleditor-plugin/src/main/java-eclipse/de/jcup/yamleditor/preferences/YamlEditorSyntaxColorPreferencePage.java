package de.jcup.yamleditor.preferences;
/*
 * Copyright 2017 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */

import static de.jcup.yamleditor.preferences.YamlEditorSyntaxColorPreferenceConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.jcup.yamleditor.YamlEditorColorConstants;
import de.jcup.yamleditor.YamlEditorUtil;

public class YamlEditorSyntaxColorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public YamlEditorSyntaxColorPreferencePage() {
		setPreferenceStore(YamlEditorUtil.getPreferences().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		Map<YamlEditorSyntaxColorPreferenceConstants, ColorFieldEditor> editorMap = new HashMap<YamlEditorSyntaxColorPreferenceConstants, ColorFieldEditor>();
		for (YamlEditorSyntaxColorPreferenceConstants colorIdentifier: YamlEditorSyntaxColorPreferenceConstants.values()){
			ColorFieldEditor editor = new ColorFieldEditor(colorIdentifier.getId(), colorIdentifier.getLabelText(), parent);
			editorMap.put(colorIdentifier, editor);
			addField(editor);
		}
		Button restoreDarkThemeColorsButton= new Button(parent,  SWT.PUSH);
		restoreDarkThemeColorsButton.setText("Restore Defaults for Dark Theme");
		restoreDarkThemeColorsButton.setToolTipText("Same as 'Restore Defaults' but for dark themes.\n Editor makes just a suggestion, you still have to apply or cancel the settings.");
		restoreDarkThemeColorsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				/* editor colors */
				changeColor(editorMap, COLOR_NORMAL_TEXT, YamlEditorColorConstants.GRAY_JAVA);
				changeColor(editorMap, COLOR_YAML_INTERNAL_KEYWORD, YamlEditorColorConstants.MIDDLE_GREEN);
				
				changeColor(editorMap, COLOR_GSTRING, YamlEditorColorConstants.MIDDLE_ORANGE);
				changeColor(editorMap, COLOR_COMMENT, YamlEditorColorConstants.GREEN_JAVA);
				changeColor(editorMap, COLOR_EXTERNAL_COMMAND, YamlEditorColorConstants.TASK_CYAN);
				changeColor(editorMap, COLOR_KNOWN_VARIABLES, YamlEditorColorConstants.DARK_THEME_GRAY);
				changeColor(editorMap, COLOR_MAPPINGS, YamlEditorColorConstants.BRIGHT_CYAN);
				changeColor(editorMap, COLOR_BLOCK, YamlEditorColorConstants.DARK_THEME_ECHO_OUTPUT);
				
				
			}

			private void changeColor(Map<YamlEditorSyntaxColorPreferenceConstants, ColorFieldEditor> editorMap,
					YamlEditorSyntaxColorPreferenceConstants colorId, RGB rgb) {
				editorMap.get(colorId).getColorSelector().setColorValue(rgb);
			}
			
		});
			
		
	}
	
}