package com.expost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.expost.domain.WebBoard;
import com.expost.persistence.WebBoardRepository;
import com.expost.vo.PageMaker;
import com.expost.vo.PageVO;

import lombok.extern.java.Log;
@Controller
@Log
@RequestMapping("/boards")
public class WebBoardController {
	@Autowired
	private WebBoardRepository repo;
	
	@GetMapping("/list")
	public void list(@ModelAttribute("pageVO") PageVO vo, Model model){
		Pageable page = vo.makePageable(0, "bno");
		
		Page<WebBoard> result = repo.findAll(
				repo.makePredicate(    vo.getType(), vo.getKeyword() ), page);
		log.info(""+page);
		log.info(""+result);
		log.info("total page number: " + result.getTotalPages());
		
		
		model.addAttribute("result", new PageMaker(result));
	}
	
	@GetMapping("/register")
	public void registerGET(@ModelAttribute("vo")WebBoard vo) {
		log.info("register get");
	}
	
	@GetMapping("/view")
	public void view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("BNO: " + bno);
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}
	
	@PostMapping("/register")
	public String registerPOST(@ModelAttribute("vo")WebBoard vo, RedirectAttributes rttr) {
		System.out.println("register post");
		log.info("this object"+vo);
		repo.save(vo);
		rttr.addFlashAttribute("msg", "success");
		return "redirect:/boards/list";
	}
	
	@PostMapping("/modify")
	public String modifyPost(WebBoard board, PageVO vo, RedirectAttributes rttr) {
		log.info("modify webboard:" + board);
		
		repo.findById(board.getBno()).ifPresent( origin -> {
			origin.setTitle(board.getTitle());
			origin.setContent(board.getContent());
			repo.save(origin);
			
			rttr.addFlashAttribute("msg", "success");
			rttr.addAttribute("bno", origin.getBno());
			
		});
		
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());
		
		return "redirect:/boards/view";
	}
}
