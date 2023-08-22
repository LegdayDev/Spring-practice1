package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId,Model model){
        Item item = itemRepository.findbyId(itemId);
        model.addAttribute("item",item);

        return "/basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model){
        /**
         * @ModelAttribute 어노테이션이 아래와 같은 로직을 실행시켜줌
         * model.addAttribute() 메서드도 같이 해준다.
             Item item = new Item();
             item.setItemName(itemName);
             item.setPrice(price);
             item.setQuantity(quantity);

             model.addAttribute("item", item);
         */

        itemRepository.save(item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        /**
         * @ModelAttribute 어노테이션은 key 값이 없으면 클래스명의 앞글자를 소문자로 바꿔서 매핑한다
         * Item -> item 이 model.addAttribute('item', item) 이렇게 담긴다.
         */
        itemRepository.save(item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV4(Item item){
        /**
         * @ModelAttribute 은 생략가능하다.
         * String, int 등등이 아닌 게 파라미터로 오면 알아서 @ModelAttribute 로 인식해준다.
         */
        itemRepository.save(item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);
        /**
         * Redirect
         * Post 방식으로 url 을 리턴해주고 새로고침을 하면 마지막에 서버에 전송한 데이터를 다시 전송한다.
         * 이 때 redirect 를 이용해서 브라우저에게 재 요청을 하면 사용자에게는 Get 방식으로 요청한 View 를 준다.
         */
        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        /**
         * RedirectAttributes
         * 상품저장이 잘 됐으면 사용자에게 "저장되었습니다" 라는 메시지를 보여달라는 요구사항이 생겼다.
         */
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findbyId(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,@ModelAttribute Item item){
        itemRepository.update(itemId,item);

        return "redirect:/basic/items/{itemId}";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemB",20000,20));
    }



}
