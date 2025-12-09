package study.ronoyaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import study.ronoyaro.domain.ConsoleGamer;
import study.ronoyaro.request.ConsolePostRequest;
import study.ronoyaro.response.ConsoleGetResponse;
import study.ronoyaro.response.ConsolePostResponse;


@Mapper
public interface ConsoleMapper {
    ConsoleMapper INSTANCE = Mappers.getMapper(ConsoleMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 150))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    ConsoleGamer toConsole(ConsolePostRequest consolePostRequest);

    ConsoleGetResponse toConsoleGetResponse(ConsoleGamer consoleGamer);
    ConsolePostResponse toConsolePostResponse(ConsoleGamer consoleGamer);

}
