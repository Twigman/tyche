package de.qwyt.housecontrol.tyche.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLight;
import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileType;
import de.qwyt.housecontrol.tyche.repository.light.HueLightRepository;
import de.qwyt.housecontrol.tyche.repository.light.HueLightStateRepository;

@ExtendWith(MockitoExtension.class)
public class LightServiceImplTest {

	@Mock
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private HueLightRepository hueLightRepository;

    @Mock
    private HueLightStateRepository hueLightStateRepository;

    @Mock
    private DeconzApiClient deconzApiClient;
    
    @Mock
    private HueColorProfileManager colorProfileManager;

    @InjectMocks
    private LightServiceImpl lightService;
	
    @BeforeEach
    void setUp() {
        // Initialisiere die lightMap mit einem Testlicht
        HueLightState state = new HueLightState();
        state.setEnabled(false);
        state.setAlert("none");
        state.setBri(100);
        state.setHue(8417);
        state.setReachable(true);
        state.setColormode("xy");
        state.setId("abc");
        state.setLightId("123");
        
        HueLight testLight = new HueLight();
        testLight.setUniqueId("123");
        testLight.setName("Test Light");
        testLight.setHascolor(true);
        testLight.setManufacturer("Philips");
        testLight.setName("Livingroom Light1 Hue");
        testLight.setState(state);

        lightService = new LightServiceImpl(objectMapper, modelMapper, hueLightRepository, hueLightStateRepository, deconzApiClient, colorProfileManager);
        lightService.getLightMap().put(testLight.getUniqueId(), testLight);
    }
    
    @Test
    void updateLightState_Success() {
    	HueLight testLight = lightService.getLightMap().get("123");
    	HueLightState oldState = modelMapper.map(testLight.getState(), HueLightState.class);
    	
    	HueLightState newState = new HueLightState();
    	newState.setEnabled(true);
    	newState.setBri(200);
    	
    	when(deconzApiClient.updateLightState(anyString(), any(HueLightState.class))).thenReturn(true);
    	
    	boolean result = lightService.updateLight("123", newState, HueColorProfileType.DEFAULT_CT_BRI);
    	
    	assertTrue(result);
    	HueLightState updatedState = lightService.getLightMap().get("123").getState();
    	// changed fields
    	assertTrue(updatedState.getBri().equals(newState.getBri()));
    	assertTrue(updatedState.getEnabled().equals(newState.getEnabled()));
    	// other fields
    	assertTrue(updatedState.getAlert().equals(oldState.getAlert()));
    	assertTrue(updatedState.getColormode().equals(oldState.getColormode()));
    	assertTrue(updatedState.getHue().equals(oldState.getHue()));
    	assertTrue(updatedState.getReachable().equals(oldState.getReachable()));
    	assertTrue(updatedState.getId().equals(oldState.getId()));
    	assertTrue(updatedState.getLightId().equals(oldState.getLightId()));
    	
    	verify(modelMapper).map(newState, lightService.getLightMap().get("1234").getState());
        verify(deconzApiClient).updateLightState(eq("123"), any(HueLightState.class));
        verify(hueLightStateRepository, never()).saveNew(any(HueLightState.class));
    	
    }
    
}
