package com.example.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Category;
import com.example.entity.Item;
import com.example.form.ItemForm;
import com.example.service.CategoryService;
import com.example.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	private final ItemService itemService;
	private final CategoryService categoryService;
	
	@Autowired
	public ItemController(ItemService itemService, CategoryService categoryService) {
		this.itemService =itemService;
		this.categoryService = categoryService;
	}

    // 商品一覧の表示
    @GetMapping
    public String index(Model model) {
        // DELETED_ATがnullのデータのみを検索します
        List<Item> items = this.itemService.findByDeletedAtIsNull();
        // 画面で利用する変数としてitemsをセットします
        model.addAttribute("items", items);
        // templates\item\index.htmlを表示します
        return "item/index";
    }

    // 商品登録ページ表示用
    @GetMapping("toroku")
    public String torokuPage(@ModelAttribute("itemForm") ItemForm itemForm, Model model) {
        // 処理を追加
    	List<Category> categories = this.categoryService.findAll();
    	model.addAttribute("categories", categories);
        return "item/torokuPage";
    }

    // 商品登録の実行
    @PostMapping("toroku")
    public String toroku(ItemForm itemForm) {
        // 処理を追加
    	this.itemService.save(itemForm);
        return "redirect:/item";
    }

    // 商品編集ページ
    @GetMapping("henshu/{id}")
    public String henshuPage(@PathVariable("id") Integer id, Model model
                             , @ModelAttribute("itemForm") ItemForm itemForm) {
        Item item = this.itemService.findById(id);
        itemForm.setName(item.getName());
        itemForm.setPrice(item.getPrice());

        // カテゴリIDをformにセットする
        itemForm.setCategoryId(item.getCategoryId());

        // Categoryモデルから一覧を取得する
        List<Category> categories = this.categoryService.findAll();

        model.addAttribute("id", id);

        // viewにカテゴリを渡す
        model.addAttribute("categories", categories);

        return "item/henshuPage";
    }

    // 商品編集の実行
    @PostMapping("henshu/{id}")
    public String henshu(@PathVariable("id") Integer id, ItemForm itemForm) {
        this.itemService.update(id, itemForm);
        return "redirect:/item";
    }

    // 商品削除の実行
    @PostMapping("sakujo/{id}")
    public String sakujo(@PathVariable("id") Integer id) {
        this.itemService.delete(id);
        return "redirect:/item";
    }
    
    

}