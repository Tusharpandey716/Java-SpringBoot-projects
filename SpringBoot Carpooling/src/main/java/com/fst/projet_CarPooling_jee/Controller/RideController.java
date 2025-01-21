package com.fst.projet_CarPooling_jee.Controller;

import com.fst.projet_CarPooling_jee.Entity.Review;
import com.fst.projet_CarPooling_jee.Entity.Ride;
import com.fst.projet_CarPooling_jee.Entity.User;
import com.fst.projet_CarPooling_jee.Repository.UserRepository;
import com.fst.projet_CarPooling_jee.Service.impl.ReviewService;
import com.fst.projet_CarPooling_jee.Service.impl.RideService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

@Controller
public class RideController {

    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @Autowired
    private RideService rideService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewService reviewService;



    @GetMapping("/showNewRidesForm")
    public String showNewRidesForm(Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // Si l'utilisateur n'est pas connecté, sauvegardez la page initiale visée
            session.setAttribute("redirectAfterLogin", "/showNewRidesForm");
            return "redirect:/loginn";
        }

        model.addAttribute("loggedInUser", loggedInUser); // Ajouter l'utilisateur au modèle
        model.addAttribute("ride", new Ride()); // Ajoutez un objet ride pour le binding
        return "addRideForm"; // Vue de formulaire de ride
    }

    // Ajouter un nouveau ride
    @PostMapping("/saveRide")
    public String saveRide(@ModelAttribute("ride") Ride ride, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginn"; // Sécurité supplémentaire
        }
        ride.setDriver(loggedInUser); // Associez l'utilisateur connecté comme conducteur
        rideService.saveRide(ride); // Sauvegarder le ride dans la BD
        return "redirect:/myRides"; // Rediriger vers une liste des rides
    }






    @GetMapping("/myRides")
    public String myRides(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
        if (loggedInUserId == null) {
            return "redirect:/login"; // Rediriger vers la page de connexion si non connecté
        }

        // Récupérer l'utilisateur depuis la base de données ou le service
        User loggedInUser = (User) session.getAttribute("loggedInUser"); // Exemple
        model.addAttribute("loggedInUser", loggedInUser);

        // Récupérer les trajets créés par l'utilisateur
        List<Ride> userRides = rideService.findRidesByDriverId(loggedInUserId);
        model.addAttribute("userRides", userRides);

        // Appeler la méthode de pagination avec le numéro de page initial (1)
        //return findPaginated(1,model);
        return "myRides";

    }

    @GetMapping("/searchRides")
    public String searchRides(
            @RequestParam(value = "startLocation", required = false) String startLocation,
            @RequestParam(value = "endLocation", required = false) String endLocation,
            @RequestParam(value = "departureDate", required = false) String departureDate,
            @RequestParam(value = "nbPassengers", required = false) Integer nbPassengers,
            @RequestParam(value = "sortOption", required = false) String sortOption,
            Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        logger.info("Search parameters received: startLocation={}, endLocation={}, departureDate={}, nbPassengers={}",
                startLocation, endLocation, departureDate, nbPassengers);

        // Paramètres de tri par défaut
        String sortField = "pricePerSeat";
        String sortDir = "asc";

        // Gérer le tri basé sur l'option sélectionnée
        if (sortOption != null) {
            switch (sortOption) {
                case "ascendingPrice":
                    sortField = "pricePerSeat";
                    sortDir = "asc";
                    break;
                case "descendingPrice":
                    sortField = "pricePerSeat";
                    sortDir = "desc";
                    break;
                case "closestDeparture":
                    sortField = "departureDate";
                    sortDir = "asc";
                    break;
                default:
                    break;
            }
        }

        boolean searchPerformed = (startLocation != null && !startLocation.isEmpty()) ||
                (endLocation != null && !endLocation.isEmpty()) ||
                (departureDate != null && !departureDate.isEmpty()) ||
                (nbPassengers != null);

        List<Ride> listRides = null;

        // Execute search only if at least one parameter is provided
        if (searchPerformed) {
            Date sqlDate = null;
            if (departureDate != null && !departureDate.isEmpty()) {
                try {
                    sqlDate = Date.valueOf(departureDate); // Convert string to SQL Date
                } catch (IllegalArgumentException e) {
                    model.addAttribute("error", "Invalid date format. Use yyyy-MM-dd.");
                    return "searchRides"; // Return to the search page with an error
                }
            }
            listRides = rideService.findRides(startLocation, endLocation, sqlDate, nbPassengers);

            // Apply sorting logic manually
            if ("ascendingPrice".equals(sortOption)) {
                listRides.sort(Comparator.comparing(Ride::getPricePerSeat));
            } else if ("descendingPrice".equals(sortOption)) {
                listRides.sort(Comparator.comparing(Ride::getPricePerSeat).reversed());
            } else if ("closestDeparture".equals(sortOption)) {
                listRides.sort(Comparator.comparing(Ride::getDepartureDate));
            }
        }

        model.addAttribute("searchPerformed", searchPerformed);
        model.addAttribute("listRides", listRides);
        model.addAttribute("sortOption", sortOption);  // Add sort option to the model
        model.addAttribute("startLocation", startLocation);
        model.addAttribute("endLocation", endLocation);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("nbPassengers", nbPassengers);

        return "searchRides";  // Return the view for displaying search results
    }



    @GetMapping("/rideDetails/{id}")
    public String showRideDetails(@PathVariable("id") Long rideId, Model model,HttpSession session) {
        // Récupérer l'utilisateur depuis la base de données ou le service
        User loggedInUser = (User) session.getAttribute("loggedInUser"); // Exemple
        model.addAttribute("loggedInUser", loggedInUser);

        Ride ride = rideService.getRideById(rideId);

        if (ride == null) {
            // Redirige vers une page d'erreur ou retourne un message approprié
            System.out.println("ride not found");
        }


        List<Review> reviews = reviewService.findReviewsByRideId(rideId);
        model.addAttribute("ride", ride);
        model.addAttribute("reviews", reviews);

        return "rideDetails";
    }








}
