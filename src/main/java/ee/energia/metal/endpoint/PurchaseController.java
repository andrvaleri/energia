package ee.energia.metal.endpoint;

import ee.energia.metal.entity.Purchase;
import ee.energia.metal.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchases")
public class PurchaseController {
    private final PurchaseService service;

    @GetMapping("")
    @Operation()
    public CollectionModel<Purchase> getAvailablePurchases() {
        var incompletePurchases = service.getIncompletePurchases();
        var links = createLinks(incompletePurchases);
        return CollectionModel.of(incompletePurchases, links);
    }

    @PostMapping("/create")
    public EntityModel<Purchase> create(
            @org.springframework.web.bind.annotation.RequestBody
            @RequestBody(content = @Content(examples = {
                    @ExampleObject(value = "B, C, W")})) String bakeGoods) {
        var purchase = service.createPurchase(bakeGoods);
        var id = purchase.getId();
        return EntityModel.of(purchase,
                linkTo(methodOn(PurchaseController.class).pay(id, "amount"))
                        .withRel(id + "/pay/")
                        .withType("POST"));
    }

    @PostMapping("{id}/pay")
    public EntityModel<Purchase> pay(
            @PathVariable @Parameter(in = PATH, example = "5") Long id,
            @org.springframework.web.bind.annotation.RequestBody
            @RequestBody(content = @Content(examples = {
                    @ExampleObject(value = "$4")})) String amountPaid) {
        return EntityModel.of(service.pay(id, amountPaid),
                linkTo(methodOn(PurchaseController.class).pay(id, amountPaid)).withSelfRel().withType("POST"));
    }

    private List<Link> createLinks(Collection<Purchase> incompletePurchases) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(PurchaseController.class).create("bakes"))
                .withRel("/create")
                .withType("POST"));
        incompletePurchases.stream()
                .findFirst()
                .ifPresent(purchase -> links.add(createPayLink(purchase)));
        return links;
    }

    private Link createPayLink(Purchase purchase) {
        return linkTo(methodOn(PurchaseController.class).pay(purchase.getId(), "money"))
                .withRel("/" + purchase.getId() + "/pay/")
                .withType("POST");
    }
}
