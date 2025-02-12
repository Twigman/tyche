package de.qwyt.housecontrol.tyche.model.light.hue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import de.qwyt.housecontrol.tyche.model.light.hue.capabilities.HueLightCapColor;

public class HueLightTest {
	
	@Test
	public void testHasChangedValuesRelevantForDatabase_FieldsChanged() {
		HueLight light1 = new HueLight();
        light1.setUniqueId("123");
        light1.setColorcapabilities(12);
        light1.setCtmax(6500);
        light1.setCtmin(2700);
        light1.setHascolor(true);
        light1.setEtag("etag1");
        light1.setModelId("model1");
        light1.setManufacturer("manufacturer1");
        light1.setName("light1");
        light1.setSwversion("1.0");
        light1.setType("type1");

        HueLight light2 = new HueLight();
        light2.setUniqueId("123");  // Same
        light2.setColorcapabilities(25); // Different
        light2.setCtmax(6500); // Same
        light2.setCtmin(2700); // Same
        light2.setHascolor(true); // Same
        light2.setEtag("etag1"); // Same
        light2.setModelId("model1"); // Same
        light2.setManufacturer("manufacturer1"); // Same
        light2.setName("light1"); // Same
        light2.setSwversion("1.0"); // Same
        light2.setType("type1"); // Same
        
        boolean result = light1.hasChangedValuesRelevantForDatabase(light2);
        
        assertTrue(result, "Expected true because colorcapabilities field has changed");
	}

	@Test
	public void testHasChangedValuesRelevantForDatabase_FieldsNotChanged() {
		HueLight light1 = new HueLight();
        light1.setUniqueId("123");
        light1.setColorcapabilities(12);
        light1.setCtmax(6500);
        light1.setCtmin(2700);
        light1.setHascolor(true);
        light1.setEtag("etag1");
        light1.setModelId("model1");
        light1.setManufacturer("manufacturer1");
        light1.setName("light1");
        light1.setSwversion("1.0");
        light1.setType("type1");
        
        HueLight light2 = new HueLight();
        light2.setUniqueId("123");  // Same
        light2.setColorcapabilities(12); // Same
        light2.setCtmax(6500); // Same
        light2.setCtmin(2700); // Same
        light2.setHascolor(true); // Same
        light2.setEtag("etag1"); // Same
        light2.setModelId("model1"); // Same
        light2.setManufacturer("manufacturer1"); // Same
        light2.setName("light1"); // Same
        light2.setSwversion("1.0"); // Same
        light2.setType("type1"); // Same
        
        boolean result = light1.hasChangedValuesRelevantForDatabase(light2);
        
        assertFalse(result, "Expected false because no fields have changed");
	}
	
	@Test
	public void testHasChangedValuesRelevantForDatabase_ConfigChanged() {
		HueLightConfigAttr config1 = new HueLightConfigAttr();
        ArrayList<String> groups1 = new ArrayList<>();
        groups1.add("0");
        groups1.add("3");
        groups1.add("6");
        config1.setGroups(groups1);
        
		HueLight light1 = new HueLight();
        light1.setUniqueId("123");
        light1.setColorcapabilities(12);
        light1.setCtmax(6500);
        light1.setCtmin(2700);
        light1.setHascolor(true);
        light1.setEtag("etag1");
        light1.setModelId("model1");
        light1.setManufacturer("manufacturer1");
        light1.setName("light1");
        light1.setSwversion("1.0");
        light1.setType("type1");
        light1.setConfig(config1);
        
        HueLightConfigAttr config2 = new HueLightConfigAttr();
        ArrayList<String> groups2 = new ArrayList<>();
        groups2.add("0"); // Same
        groups2.add("2"); // Different
        groups2.add("6"); // Same
        config2.setGroups(groups2);
        
        HueLight light2 = new HueLight();
        light2.setUniqueId("123");  // Same
        light2.setColorcapabilities(12); // Same
        light2.setCtmax(6500); // Same
        light2.setCtmin(2700); // Same
        light2.setHascolor(true); // Same
        light2.setEtag("etag1"); // Same
        light2.setModelId("model1"); // Same
        light2.setManufacturer("manufacturer1"); // Same
        light2.setName("light1"); // Same
        light2.setSwversion("1.0"); // Same
        light2.setType("type1"); // Same
        light2.setConfig(config2);
        
        boolean result = light1.hasChangedValuesRelevantForDatabase(light2);
        
        assertTrue(result, "Expected true because one group in the config changed");
	}
	
	@Test
	public void testHasChangedValuesRelevantForDatabase_ConfigNotChanged() {
		HueLightConfigAttr config1 = new HueLightConfigAttr();
        ArrayList<String> groups1 = new ArrayList<>();
        groups1.add("0");
        groups1.add("3");
        groups1.add("6");
        config1.setGroups(groups1);
		
		HueLight light1 = new HueLight();
        light1.setUniqueId("123");
        light1.setColorcapabilities(12);
        light1.setCtmax(6500);
        light1.setCtmin(2700);
        light1.setHascolor(true);
        light1.setEtag("etag1");
        light1.setModelId("model1");
        light1.setManufacturer("manufacturer1");
        light1.setName("light1");
        light1.setSwversion("1.0");
        light1.setType("type1");
        light1.setConfig(config1);
        
        
        HueLightConfigAttr config2 = new HueLightConfigAttr();
        ArrayList<String> groups2 = new ArrayList<>();
        groups2.add("0"); // Same
        groups2.add("3"); // Same
        groups2.add("6"); // Same
        config2.setGroups(groups2);
        
        HueLight light2 = new HueLight();
        light2.setUniqueId("123");  // Same
        light2.setColorcapabilities(12); // Same
        light2.setCtmax(6500); // Same
        light2.setCtmin(2700); // Same
        light2.setHascolor(true); // Same
        light2.setEtag("etag1"); // Same
        light2.setModelId("model1"); // Same
        light2.setManufacturer("manufacturer1"); // Same
        light2.setName("light1"); // Same
        light2.setSwversion("1.0"); // Same
        light2.setType("type1"); // Same
        light2.setConfig(config2);
        
        boolean result = light1.hasChangedValuesRelevantForDatabase(light2);
        
        assertFalse(result, "Expected false because the config is the same");
	}
	
	@Test
	public void testHasChangedValuesRelevantForDatabase_ColorChanged() {
		HueLightConfigAttr config1 = new HueLightConfigAttr();
        ArrayList<String> groups1 = new ArrayList<>();
        groups1.add("0");
        groups1.add("3");
        groups1.add("6");
        config1.setGroups(groups1);
        
        HueLightCapColor color1 = new HueLightCapColor();
        ArrayList<String> effects1 = new ArrayList<>();
        effects1.add("cool");
        effects1.add("special");
        color1.setEffects(effects1);
        
        ArrayList<String> modes1 = new ArrayList<>();
        modes1.add("mode1");
        modes1.add("mode2");
        color1.setModes(modes1);
        
        color1.setCt(null);
        
        HueLightCapabilities cap1 = new HueLightCapabilities();
        cap1.setColor(color1);
        
		HueLight light1 = new HueLight();
        light1.setUniqueId("123");
        light1.setColorcapabilities(12);
        light1.setCtmax(6500);
        light1.setCtmin(2700);
        light1.setHascolor(true);
        light1.setEtag("etag1");
        light1.setModelId("model1");
        light1.setManufacturer("manufacturer1");
        light1.setName("light1");
        light1.setSwversion("1.0");
        light1.setType("type1");
        light1.setConfig(config1);
        light1.setCapabilities(cap1);
        
        HueLightConfigAttr config2 = new HueLightConfigAttr();
        ArrayList<String> groups2 = new ArrayList<>();
        groups2.add("0"); // Same
        groups2.add("3"); // Same
        groups2.add("6"); // Same
        config2.setGroups(groups2);
        
        HueLightCapColor color2 = new HueLightCapColor();
        ArrayList<String> effects2 = new ArrayList<>();
        effects2.add("cool"); // Same
        effects2.add("special"); // Same
        color2.setEffects(effects2);
        
        ArrayList<String> modes2 = new ArrayList<>();
        modes2.add("mode1"); // Same
        modes2.add("mode3"); // Different
        color2.setModes(modes2);
        
        color2.setCt(null); // Same
        
        HueLightCapabilities cap2 = new HueLightCapabilities();
        cap2.setColor(color2);
        
        HueLight light2 = new HueLight();
        light2.setUniqueId("123");  // Same
        light2.setColorcapabilities(12); // Same
        light2.setCtmax(6500); // Same
        light2.setCtmin(2700); // Same
        light2.setHascolor(true); // Same
        light2.setEtag("etag1"); // Same
        light2.setModelId("model1"); // Same
        light2.setManufacturer("manufacturer1"); // Same
        light2.setName("light1"); // Same
        light2.setSwversion("1.0"); // Same
        light2.setType("type1"); // Same
        light2.setConfig(config2); 
        light2.setCapabilities(cap2);
        
        boolean result = light1.hasChangedValuesRelevantForDatabase(light2);
        
        assertTrue(result, "Expected true because one mode in capabilities->color changed");
	}
	
}
