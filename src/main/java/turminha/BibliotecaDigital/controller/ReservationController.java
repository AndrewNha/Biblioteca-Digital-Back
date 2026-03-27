package turminha.BibliotecaDigital.controller;

import org.springframework.web.bind.annotation.*;
import turminha.BibliotecaDigital.model.Author;
import turminha.BibliotecaDigital.model.Reservation;
import turminha.BibliotecaDigital.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    @GetMapping("/{id}")
    //LISTAR UM
    public Reservation findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.save(reservation);
    }

    @PatchMapping("/{id}/cancel")
    public Reservation cancel(@PathVariable Long id) {
        return reservationService.cancel(id);
    }

    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id, @RequestBody Reservation reservation) {
        return reservationService.update(id, reservation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }
}