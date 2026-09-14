package com.example.desarrollo.service;

import com.example.desarrollo.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitacionService {
    @Autowired
    private final HabitacionRepository habitacionRepository;
    private final ModelMapper modelMapper;

    // Create (POST)
}
