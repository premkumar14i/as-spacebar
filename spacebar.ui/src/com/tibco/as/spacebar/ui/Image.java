package com.tibco.as.spacebar.ui;

public enum Image {

	METASPACE_DISCONNECTED("metaspace_disconnected_gray.gif"), METASPACE_CONNECTED(
			"metaspace_connected.gif"), CONNECT("connect.png"), DISCONNECT(
			"disconnect.png"), SPACES("spaces.gif"), SPACE("space.gif"), FIELDS(
			"fields.gif"), FIELD("field.gif"), KEY("field_key.gif"), INDEXES(
			"indexes.gif"), INDEX("index.gif"), WAITING("waiting.gif"), SPACE_BROWSE(
			"space_browse.gif"), REFRESH_ENABLED("refresh_enabled.gif"), REFRESH_DISABLED(
			"refresh_disabled.gif"), LOCK_ENABLED("lock_enabled.gif"), LOCK_DISABLED(
			"lock_disabled.gif"), ADD("add.gif"), DELETE("delete.gif"), CSV(
			"csv.png"), EXCEL("excel.png"), WIZBAN_CSV("wizban-csv.png"), WIZBAN_EXCEL(
			"wizban-xls.png"), CONFIG("config_obj.gif"), RULER("ruler.png"), MEMBERS(
			"members.gif"), MEMBER("member.png"), MEMBER_SEEDER(
			"member_seeder.png"), DISTRIBUTION("field_distribution.gif"), WIZBAN_SIMULATION(
			"wizban-simulation.png"), NEW_WINDOW("newwindow.gif");

	private String path;

	private Image(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}