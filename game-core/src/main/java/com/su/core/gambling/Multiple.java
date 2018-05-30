package com.su.core.gambling;



public class Multiple {
	private int value;
	private int type;

	public Multiple(int type, int value) {
		this.type = type;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public PMultiple toProto() {
		PMultiple.Builder builder = PMultiple.newBuilder();
		return toProto(builder);
	}

	public PMultiple toProto(PMultiple.Builder builder) {
		builder.setType(type);
		builder.setValue(value);
		PMultiple pMultiple = builder.build();
		builder.clear();
		return pMultiple;
	}

}
