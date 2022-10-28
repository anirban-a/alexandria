package com.rpi.alexandria.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
@ToString
@Document(indexName = "book")
public class Book {

	private static Map<String, Condition> conditionMap = new HashMap<>();

	static {
		conditionMap.put("As New", Condition.AS_NEW);
		conditionMap.put("Good", Condition.GOOD);
		conditionMap.put("Nearly Damaged", Condition.NEARLY_DAMAGED);
		conditionMap.put("Damaged", Condition.DAMAGED);
	}

	@Id
	String id;

	@Field(type = FieldType.Keyword)
	@NotBlank(message = "ISBN is mandatory")
	String isbn;

	@Field(type = FieldType.Keyword)
	@NotBlank(message = "Book name is mandatory")
	String name;

	@Field(type = FieldType.Text)
	@NotBlank(message = "Book condition is mandatory")
	String condition;

	@Field(type = FieldType.Text)
	String description;

	@Field(type = FieldType.Boolean)
	Boolean forExchange;

	@Field(type = FieldType.Boolean)
	Boolean forGiveAway;

	@Field(type = FieldType.Nested)
	User listedBy; // internal property.

	public void setCondition(String condition) {
		if (!conditionMap.containsKey(condition)) {
			throw new IllegalArgumentException("Invalid Book Condition provided");
		}
		this.condition = condition;
	}

	public Condition getConcreteBookCondition() {
		return conditionMap.get(condition);
	}

	public enum Condition {

		AS_NEW, FINE, GOOD, FAIR, NEARLY_DAMAGED, DAMAGED

	}

}
