package com.green.studybridge.grade.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GradeGetRes {
    List<GradeGetDto> gradeGetDto;
}