package com.graduationProject.medicory.mapper;

import com.graduationProject.medicory.entity.phoneEntities.RelativePhoneNumber;
import com.graduationProject.medicory.model.phones.RelativePhoneNumberDTO;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface RelativePhoneMapper {

   List<RelativePhoneNumber> toEntity(List<RelativePhoneNumberDTO> relativePhoneNumberDTO);
}
