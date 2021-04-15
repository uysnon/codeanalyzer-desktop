package ru.rsreu.gorkin.codeanalyzer.core.rules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Example {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RuleStructure ruleStructure = new RuleStructure();
        ruleStructure.setValue("^m_(.*)$");
        ruleStructure.setImportance(2);
        RuleType ruleType =  new RuleType();
        ruleType.setTitle("naming");
        ruleType.setScopes(Stream.of(Scopes.LOCAL_FIELD).collect(Collectors.toList()));
        ruleStructure.setType(ruleType);
        List<RuleStructure> list = new ArrayList<>();
        list.add(ruleStructure);
        list.add(ruleStructure);
        String jsonResult = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(list);
        int a = 1;
        RuleStructure readStructure = new ObjectMapper().readValue("{\n" +
                "  \"value\": \"^m_(.*)$\",\n" +
                "  \"importance\": 2,\n" +
                "  \"type\": {\n" +
                "    \"title\": \"naming\",\n" +
                "    \"scope\": [\n" +
                "      \"LOCAL_FIELD\"\n" +
                "    ]\n" +
                "  }\n" +
                "}\n" +
                "\n", RuleStructure.class);
    }
}
