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
package de.jcup.yamleditor.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

import de.jcup.yamleditor.outline.Item;
import de.jcup.yamleditor.outline.ItemType;
import de.jcup.yamleditor.script.YamlLabel;
import de.jcup.yamleditor.script.YamlScriptModel;

public class YamlEditorTreeContentProvider implements ITreeContentProvider {

	private static final String YAML_SCRIPT_CONTAINS_ERRORS = "Yaml script contains errors.";
	/* FIXME Albert: 2018-23-05 support outline data*/
	private static final String YAML_SCRIPT_DOES_NOT_CONTAIN_ANY_LABELS = "Yaml outline not available in alpha version - will come in future";
	private static final Object[] RESULT_WHEN_EMPTY = new Object[] { YAML_SCRIPT_DOES_NOT_CONTAIN_ANY_LABELS };
	private Object[] items;
	private Object monitor = new Object();

	YamlEditorTreeContentProvider() {
		items = RESULT_WHEN_EMPTY;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		synchronized (monitor) {
			if (inputElement!=null && !(inputElement instanceof YamlScriptModel)) {
				return new Object[] { "Unsupported input element:"+inputElement };
			}
			if (items != null && items.length > 0) {
				return items;
			}
		}
		return RESULT_WHEN_EMPTY;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

	private Item[] createItems(YamlScriptModel model) {
		List<Item> list = new ArrayList<>();
		for (YamlLabel labelion : model.getLabels()) {
			Item item = new Item();
			item.name = labelion.getName();
			item.type = ItemType.LABEL;
			item.offset = labelion.getPosition();
			item.length = labelion.getLengthToNameEnd();
			item.endOffset=labelion.getEnd();
			list.add(item);
		}
		if (list.isEmpty()) {
			Item item = new Item();
			item.name = YAML_SCRIPT_DOES_NOT_CONTAIN_ANY_LABELS;
			item.type = ItemType.META_INFO;
			item.offset = 0;
			item.length = 0;
			item.endOffset=0;
			list.add(item);
		}
		if (model.hasErrors()) {
			Item item = new Item();
			item.name = YAML_SCRIPT_CONTAINS_ERRORS;
			item.type = ItemType.META_ERROR;
			item.offset = 0;
			item.length = 0;
			item.endOffset=0;
			list.add(0, item);
		}
		return list.toArray(new Item[list.size()]);

	}

	public void rebuildTree(YamlScriptModel model) {
		synchronized (monitor) {
			if (model == null) {
				items = null;
				return;
			}
			items = createItems(model);
		}
	}

	public Item tryToFindByOffset(int offset) {
		synchronized (monitor) {
			if (items==null){
				return null;
			}
			for (Object oitem: items){
				if (!(oitem instanceof Item)){
					continue;
				}
				Item item = (Item) oitem;
				int itemStart = item.getOffset();
				int itemEnd = item.getEndOffset();// old: itemStart+item.getLength();
				if (offset >= itemStart && offset<=itemEnd ){
					return item;
				}
			}
			
		}
		return null;
	}

}
