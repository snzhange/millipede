package org.animeaholic.millipede.enums;

/**
 * 任务状态枚举(暂时没有监控所以没用上)
 * @author snzhange
 */
public enum TaskStatusEnum {
	
	PENDING("等待执行"),
	SUCCESS("执行完成"),
	FAILED("执行失败");
	
	/* 状态的描述 */
	private String description;

	private TaskStatusEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
