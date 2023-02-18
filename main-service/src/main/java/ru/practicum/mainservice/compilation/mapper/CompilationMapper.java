package ru.practicum.mainservice.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto toCompilationDto(Compilation compilation);

    default Compilation toCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto == null) {
            return null;
        }
        Boolean pinned = newCompilationDto.getPinned();
        String title = newCompilationDto.getTitle();

        Compilation compilation = Compilation.builder()
                .pinned(pinned)
                .title(title)
                .build();
        return compilation;
    }
}
