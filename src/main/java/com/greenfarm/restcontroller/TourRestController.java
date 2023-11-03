package com.greenfarm.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.dto.TourImageDTO;
import com.greenfarm.entity.Pricing;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourCondition;
import com.greenfarm.entity.TourImage;
import com.greenfarm.entity.TourOverview;
import com.greenfarm.service.PricingService;
import com.greenfarm.service.TourConditionService;
import com.greenfarm.service.TourImageService;
import com.greenfarm.service.TourOverviewService;
import com.greenfarm.service.TourService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/tours")
public class TourRestController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	TourService tourService;

	@Autowired
	TourConditionService tourConditionService;

	@Autowired
	TourOverviewService tourOverviewService;

	@Autowired
	PricingService pricingService;

	@Autowired
	TourImageService tourImageService;

	@GetMapping()
	public ResponseEntity<List<TourDTO>> getList() {
		List<Tour> tours = tourService.findAll();
		List<TourDTO> tourDTOs = tours.stream().map(tour -> modelMapper.map(tour, TourDTO.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(tourDTOs, HttpStatus.OK);
	}

	@GetMapping("{tourid}")
	public ResponseEntity<TourDTO> getOne(@PathVariable("tourid") Integer tourid) {
		Tour tour = tourService.findById(tourid);

		if (tour == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		TourDTO tourDTO = modelMapper.map(tour, TourDTO.class);
		return new ResponseEntity<>(tourDTO, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<TourDTO> create(@RequestBody TourDTO tourDTO) {
		// Map DTO to entity
		Tour tour = modelMapper.map(tourDTO, Tour.class);

		// Create tour condition
		TourCondition tourCondition = new TourCondition();
		if (tourDTO.getTourCondition() != null) {
			tourCondition.setConditions(tourDTO.getTourCondition().getConditions());
			tourCondition.setTour(tour);
			tour.setTourCondition(tourCondition);
		} else {
			tourCondition.setTour(tour);
			tour.setTourCondition(tourCondition);
		}

		// Create tour overview
		TourOverview tourOverview = new TourOverview();
		if (tourDTO.getTourOverview() != null) {
			tourOverview.setTitle(tourDTO.getTourOverview().getTitle());
			tourOverview.setContent(tourDTO.getTourOverview().getContent());
			tourOverview.setTour(tour);
			tour.setTourOverview(tourOverview);
		} else {
			tourOverview.setTour(tour);
			tour.setTourOverview(tourOverview);
		}

		// Create pricings
		Pricing pricings = new Pricing();
		if (tourDTO.getPricings() != null) {
			pricings.setAdultprice(tourDTO.getPricings().getAdultprice());
			pricings.setChildprice(tourDTO.getPricings().getChildprice());
			pricings.setTour(tour);
			tour.setPricings(pricings);
		} else {
			pricings.setTour(tour);
			tour.setPricings(pricings);
		}

		// Create tour images
		List<TourImage> tourImages = new ArrayList<>();
		// nếu rỗng ko tạo
		if (tourDTO.getTourImage() != null && !tourDTO.getTourImage().isEmpty()) {
			for (TourImageDTO tourImageDTO : tourDTO.getTourImage()) {
				TourImage tourImage = new TourImage();
				tourImage.setImageurl((tourImageDTO.getImageurl()));
				tourImage.setTour(tour);
				tourImages.add(tourImage);
			}
		}
		tour.setTourImage(tourImages);

		// Create tour
		Tour createdTour = tourService.create(tour);

		// Map entity to DTO
		TourDTO createdTourDTO = modelMapper.map(createdTour, TourDTO.class);

		return new ResponseEntity<>(createdTourDTO, HttpStatus.CREATED);
	}

	@PutMapping("/{tourid}")
	public ResponseEntity<TourDTO> update(@PathVariable("tourid") Integer tourid, @RequestBody TourDTO tourDTO) {
		try {
			Tour updatedTour = updateTour(tourDTO, tourid);
			// Map entity to DTO
			TourDTO updatedTourDTO = modelMapper.map(updatedTour, TourDTO.class);
			return new ResponseEntity<>(updatedTourDTO, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.error("Lỗi xảy ra khi cập nhật tour: " + e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	private Tour updateTour(TourDTO tourDTO, Integer tourid) {
		Tour tour = tourService.findById(tourid);
		if (tour == null) {
			return null;
		}

		// Map DTO to entity
		modelMapper.map(tourDTO, tour);

		if (tourDTO.getTourCondition() != null) {
			try {
				Optional<TourCondition> tourConditionOptional = tourConditionService.findByTourId(tourid);
				TourCondition tourCondition;

				if (tourConditionOptional.isPresent()) {
					// Nếu tồn tại lấy ra -> cập nhật
					tourCondition = tourConditionOptional.get();
				} else {
					// Bản ghi không tồn tại, tạo mới
					tourCondition = new TourCondition();
				}
				tourCondition.setConditions(tourDTO.getTourCondition().getConditions());
				tourCondition.setTour(tour);
				tour.setTourCondition(tourCondition);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (tourDTO.getTourOverview() != null) {
			try {
				Optional<TourOverview> tourOverviewOptional = tourOverviewService.findByTourId(tourid);

				TourOverview tourOverview;
				if (tourOverviewOptional.isPresent()) {
					// Nếu tồn tại lấy ra -> cập nhật
					tourOverview = tourOverviewOptional.get();
				} else {
					// Bản ghi không tồn tại, tạo mới
					tourOverview = new TourOverview();
				}
				// Cập nhật thông tin từ tourDTO
				tourOverview.setTitle(tourDTO.getTourOverview().getTitle());
				tourOverview.setContent(tourDTO.getTourOverview().getContent());
				tourOverview.setTour(tour);
				tour.setTourOverview(tourOverview);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (tourDTO.getPricings() != null) {
			// Update pricings
			Pricing pricings = pricingService.findByTourId(tourid);
			if (pricings == null) {
				pricings = new Pricing();
				pricings.setTour(tour);
			}
			pricings.setAdultprice(tourDTO.getPricings().getAdultprice());
			pricings.setChildprice(tourDTO.getPricings().getChildprice());
			pricings.setTour(tour);
			tour.setPricings(pricings);
		}

		List<TourImage> tourImages = new ArrayList<>(tour.getTourImage());
		List<TourImage> tempTourImages = new ArrayList<>();
		List<TourImage> tourImagesToDelete = new ArrayList<>();

		for (TourImageDTO tourImageDTO : tourDTO.getTourImage()) {
		    Integer tourImageId = tourImageDTO.getTourimageid();;
		    Optional<TourImage> optionalTourImage = tourImages.stream()
                    .filter(ti -> ti.getTourimageid() != null && ti.getTourimageid().equals(tourImageId))
                    .findFirst();
		    if (optionalTourImage.isPresent()) {
		        TourImage tourImage = optionalTourImage.get();
		        tourImage.setImageurl(tourImageDTO.getImageurl());
		        tourImage.setTour(tour);
		        tempTourImages.add(tourImage);
		    } else {
		        TourImage tourImage = new TourImage();
		        tourImage.setImageurl(tourImageDTO.getImageurl());
		        tourImage.setTour(tour);
		        tourImageService.save(tourImage);
		        tempTourImages.add(tourImage);
		    }
		}

		for (TourImage tourImage : tourImages) {
		    if (!tempTourImages.contains(tourImage)) {
		        tourImagesToDelete.add(tourImage);
		    }
		}

		tourImages.removeAll(tourImagesToDelete);
		tourImages.addAll(tempTourImages);

		for (TourImage tourImage : tourImagesToDelete) {
		    tourImageService.delete(tourImage);
		}
		
		tour.setTourImage(tourImages);
		return tourService.update(tour);
	}

	@DeleteMapping("{tourid}")
	public ResponseEntity<Void> delete(@PathVariable("tourid") Integer tourid) {
		tourService.delete(tourid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}