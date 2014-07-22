package com.tibco.as.spacebar.ui;

public enum Image {

	METASPACE_DISCONNECTED("metaspace_disconnected.png"), METASPACE_CONNECTED(
			"metaspace_connected.png"), SPACES("spaces.png"), SPACE("space.png"), FIELDS(
			"fields.png"), FIELD("field.png"), INDEXES("indexes.png"), INDEX(
			"index.png"), LOCK_ENABLED("lock_enabled.png"), LOCK_DISABLED(
			"lock_disabled.png"), ADD("add_obj.png"), DELETE("delete.png"), CSV(
			"csv.png"), EXCEL("excel.png"), WIZBAN_CSV("wizban-csv.png"), WIZBAN_EXCEL(
			"wizban-xls.png"), CONFIG("configs.png"), RULER("ruler.png"), MEMBERS(
			"members.png"), MEMBER("member.png"), MEMBER_SEEDER(
			"member_seeder.png"), DISTRIBUTION("field_distribution.png"), WIZBAN_SIMULATION(
			"wizban-simulation.png"), NEW_WINDOW("newwindow.gif");

	private String path;

	private Image(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}