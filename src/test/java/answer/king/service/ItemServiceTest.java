package answer.king.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import answer.king.exception.InvalidItemException;
import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
public class ItemServiceTest extends TestCase {

	@Configuration
	static class Config {
		
		@Bean
		public ItemRepository itemRepository() {
			return Mockito.mock(ItemRepository.class);
		}
		
		@Bean
		public ItemService itemService() {
			return new ItemService();
		}
		
	}
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Test
	public void testSaveWithValidItem() {
		Item item = createItem(null, "Burger", new BigDecimal("1.99"));
		Item persistedItem = createItem(1L, "Burger", new BigDecimal("1.99"));
		
		Mockito.when(itemRepository.save(item)).thenReturn(persistedItem);
		
		Item returnedItem = itemService.save(item);
		assertEquals(persistedItem.getId(), returnedItem.getId());
		assertEquals(persistedItem.getName(), returnedItem.getName());
		assertEquals(persistedItem.getPrice(), returnedItem.getPrice());
		assertNull(returnedItem.getOrder());
	}
	
	@Test
	public void testSaveWithMissingName() {
		boolean exceptionThrown = false;
		
		try {
			Item item = createItem(null, "", new BigDecimal("1.99"));
			@SuppressWarnings("unused")
			Item returnedItem = itemService.save(item);	
		} catch (InvalidItemException e) {
			exceptionThrown = true;
			assertEquals("No name specified", e.getMessage());
		}
		
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testSaveWithMissingPrice() {
		boolean exceptionThrown = false;
		
		try {
			Item item = createItem(null, "Burger", null);
			@SuppressWarnings("unused")
			Item returnedItem = itemService.save(item);	
		} catch (InvalidItemException e) {
			exceptionThrown = true;
			assertEquals("No price specified", e.getMessage());
		}
		
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testSaveWithNegativePrice() {
		boolean exceptionThrown = false;
		
		try {
			Item item = createItem(null, "Burger", new BigDecimal("-1.99"));
			@SuppressWarnings("unused")
			Item returnedItem = itemService.save(item);	
		} catch (InvalidItemException e) {
			exceptionThrown = true;
			assertEquals("Price must be greater than zero", e.getMessage());
		}
		
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testSaveWithZeroPrice() {
		boolean exceptionThrown = false;
		
		try {
			Item item = createItem(null, "Burger", new BigDecimal("0"));
			@SuppressWarnings("unused")
			Item returnedItem = itemService.save(item);	
		} catch (InvalidItemException e) {
			exceptionThrown = true;
			assertEquals("Price must be greater than zero", e.getMessage());
		}
		
		assertTrue(exceptionThrown);
	}
	
	private Item createItem(Long id, String name, BigDecimal price) {
		Item item = new Item();
		item.setId(id);
		item.setName(name);
		item.setPrice(price);
		return item;
	}
	
}
