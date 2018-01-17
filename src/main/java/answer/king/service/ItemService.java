package answer.king.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.exception.InvalidItemException;
import answer.king.model.Item;
import answer.king.repo.ItemRepository;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public List<Item> getAll() {
		return itemRepository.findAll();
	}

	public Item save(Item item) throws InvalidItemException {
		validateItem(item);
		return itemRepository.save(item);
	}
	
	private void validateItem(Item item) throws InvalidItemException {
		if (StringUtils.isBlank(item.getName())) {
			throw new InvalidItemException("No name specified");
		}
		if (item.getPrice() == null) {
			throw new InvalidItemException("No price specified");
		}
		if (item.getPrice().doubleValue() <= 0) {
			throw new InvalidItemException("Price must be greater than zero");
		}
	}
	
}
