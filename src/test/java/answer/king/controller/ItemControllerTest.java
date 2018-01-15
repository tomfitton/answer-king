package answer.king.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import answer.king.service.ItemService;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {

	@Configuration
	static class Config {
		
		@Bean
		public ItemRepository itemRepository() {
			return Mockito.mock(ItemRepository.class);
		}
		
		@Bean
		public ItemService itemService() {
			return Mockito.mock(ItemService.class);
		}
		
		@Bean
		public ItemController itemController() {
			return new ItemController();
		}
		
	}
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ItemService itemService;
	
	@Test
	public void testGet() throws Exception {
		Item item = createItem(null, "Burger", new BigDecimal("1.99"));
		
		List<Item> items = new ArrayList<Item>();
		items.add(item);
		
		Mockito.when(itemService.getAll()).thenReturn(items);
		
		mvc.perform(get("/item")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(not(empty()))))
				.andExpect(jsonPath("$[0].name", is("Burger")))
				.andExpect(jsonPath("$[0].price", is(1.99)));
	}
	
	@Test
	public void testCreate() throws Exception {
		Item item = createItem(1L, "Burger", new BigDecimal("1.99"));;
		Item persistedItem = createItem(1L, "Burger", new BigDecimal("1.99"));;
		
		Mockito.when(itemService.save(Mockito.any())).thenReturn(persistedItem);
		
		mvc.perform(post("/item")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonBytes(item)))
				.andExpect(status().isOk()) 
				.andExpect(jsonPath("$.id", is(not(empty()))))
				.andExpect(jsonPath("$.name", is("Burger")))
				.andExpect(jsonPath("$.price", is(1.99)));
	}
	
	private byte[] convertObjectToJsonBytes(Object object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
	}
	
	private Item createItem(Long id, String name, BigDecimal price) {
		Item item = new Item();
		item.setId(id);
		item.setName(name);
		item.setPrice(price);
		return item;
	}
	
}
