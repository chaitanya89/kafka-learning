package com.sapient.kafka;

import java.util.Arrays;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Serialized;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding(KafkaStreamsProcessor.class)
public class ComicsProcessor {

	public static final String INPUT_TOPIC = "input";
	public static final String OUTPUT_TOPIC = "output";

	@StreamListener(INPUT_TOPIC)
	@SendTo(OUTPUT_TOPIC)
	public KStream<String, Long> processCharacters(KStream<String, String> sentences) {

		return sentences.flatMapValues(sentence -> Arrays.asList(sentence.split("\\s+")))
				.map((key, value) -> new KeyValue<>(value, value))
				.groupByKey(Serialized.with(Serdes.String(), Serdes.String())).count().toStream();

	}

}
