package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    // 동시에 여러 스레드가 접근하면 HashMap 을 사용하면 안된다.
    // 사용할려면 ConcurrentHashMap 을 사용해야한다. 이유는?

    private static long sequence = 0L;
    // 얘도 동시성 문제때문에 long 이 아닌 AtomicLong 을 사용해야 한다.

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(),item);

        return item;
    }

    public Item findbyId(Long id){
        return store.get(id);
    }
    public List<Item> findAll(){
        return new ArrayList<>(store.values());
        //ArrayList 로 감싼 이유는 ArrayList 에 값이 추가되도 실제 store 에는 영향이 없기 때문
    }
    public void update(Long itemId, Item updateParam){
        Item findItem = findbyId(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        //setter 보다는 별도의 DTO 객체를 만들어서 사용하는게 좋다.
    }

    public void clearStore(){
        store.clear(); // test 를 위해 생성
    }
}
