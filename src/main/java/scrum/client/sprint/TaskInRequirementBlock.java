/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package scrum.client.sprint;

import ilarkesto.gwt.client.AnchorPanel;
import scrum.client.collaboration.EmoticonsWidget;
import scrum.client.common.ABlockWidget;
import scrum.client.common.AScrumAction;
import scrum.client.common.BlockHeaderWidget;
import scrum.client.common.BlockWidgetFactory;
import scrum.client.dnd.TrashSupport;
import scrum.client.img.Img;
import scrum.client.journal.ActivateChangeHistoryAction;
import scrum.client.tasks.TaskWidget;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TaskInRequirementBlock extends ABlockWidget<Task> implements TrashSupport {

	private AnchorPanel statusIcon;

	@Override
	protected void onInitializationHeader(BlockHeaderWidget header) {
		Task task = getObject();
		statusIcon = header.addIconWrapper();
		header.addText(task.getLabelModel());
		header.addText(task.getOwnerModel(), true);
		header.appendOuterCell(new EmoticonsWidget(task), null, true);
		header.addMenuAction(new ClaimTaskAction(task));
		header.addMenuAction(new CloseTaskAction(task));
		header.addMenuAction(new ReopenTaskAction(task));
		header.addMenuAction(new UnclaimTaskAction(task));
		header.addMenuAction(new CreateTaskImpedimentAction(task));
		header.addMenuAction(new DeleteTaskAction(task));
		header.addMenuAction(new ActivateChangeHistoryAction(task));
	}

	@Override
	protected void onUpdateHeader(BlockHeaderWidget header) {
		Task task = getObject();
		header.setDragHandle(task.getReference());
		Image statusImage = null;
		if (task.isClosed()) {
			statusImage = Img.tskClosed();
			statusImage.setTitle("Closed.");
			task.getBurnedWork();
		} else if (task.isBlocked()) {
			statusImage = Img.tskBlocked();
			statusImage.setTitle("Blocked by " + task.getImpediment().getReferenceAndLabel() + ".");
		} else if (task.isOwnerSet()) {
			statusImage = Img.tskClaimed();
			statusImage.setTitle("Claimed by " + task.getOwner().getName() + ".");
		}
		statusIcon.setWidget(statusImage);
	}

	@Override
	protected Widget onExtendedInitialization() {
		return new TaskWidget(getObject());
	}

	@Override
	public AScrumAction getTrashAction() {
		return new DeleteTaskAction(getObject());
	}

	public static final BlockWidgetFactory<Task> FACTORY = new BlockWidgetFactory<Task>() {

		@Override
		public TaskInRequirementBlock createBlock() {
			return new TaskInRequirementBlock();
		}
	};
}
