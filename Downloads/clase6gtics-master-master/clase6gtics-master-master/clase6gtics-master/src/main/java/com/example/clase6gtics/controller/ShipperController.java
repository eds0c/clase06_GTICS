package com.example.clase6gtics.controller;

import com.example.clase6gtics.entity.Shipper;
import com.example.clase6gtics.repository.ShipperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    final ShipperRepository shipperRepository;

    public ShipperController(ShipperRepository shipperRepository) {
        this.shipperRepository = shipperRepository;
    }

    @GetMapping(value = {"/list", ""})
    public String listarTransportistas(Model model) {

        List<Shipper> lista = shipperRepository.findAll();
        model.addAttribute("shipperList", lista);

        return "shipper/list";
    }

    @GetMapping("/new")
    public String nuevoTransportistaFrm(@ModelAttribute("shipper") Shipper shipper, Model model) {
        return "shipper/editFrm";
    }

    @PostMapping("/save")
    public String guardarNuevoTransportista(@ModelAttribute("shipper") Shipper shipper, Model model, RedirectAttributes attr) {

        if (shipper.getCompanyName().equals("")) {
            model.addAttribute("errorCompany", "El nombre no puede ser vacío");
            return "shipper/editFrm";
        } else {
            if (shipper.getShipperId() == 0) {
                attr.addFlashAttribute("msg", "Usuario creado exitosamente");
            } else {
                attr.addFlashAttribute("msg", "Usuario actualizado exitosamente");
            }
            shipperRepository.save(shipper);
            return "redirect:/shipper/list";
        }
    }

    @GetMapping("/edit")
    public String editarTransportista(@ModelAttribute("shipper") Shipper shipper,
                                      Model model,
                                      @RequestParam("id") int id) {

        Optional<Shipper> optShipper = shipperRepository.findById(id);

        if (optShipper.isPresent()) {
            shipper = optShipper.get();
            model.addAttribute("shipper", shipper);
            return "shipper/editFrm";
        } else {
            return "redirect:/shipper/list";
        }
    }

    @GetMapping("/delete")
    public String borrarTransportista(Model model,
                                      @RequestParam("id") int id,
                                      RedirectAttributes attr) {

        Optional<Shipper> optShipper = shipperRepository.findById(id);

        if (optShipper.isPresent()) {
            shipperRepository.deleteById(id);
            attr.addFlashAttribute("msg", "Transportista borrado exitosamente");
        }
        return "redirect:/shipper/list";

    }

    @PostMapping("/BuscarTransportistas")
    public String buscarTransportista(@RequestParam("searchField") String searchField,
                                      Model model) {

        List<Shipper> listaTransportistas = shipperRepository.buscarTransPorCompName(searchField);
        model.addAttribute("shipperList", listaTransportistas);

        return "shipper/list";
    }


}

