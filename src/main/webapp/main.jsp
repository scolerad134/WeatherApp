<%@ page import="java.util.Map" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="/" method="get">
    <input name="city" placeholder="City" type="text">
    <button type="submit">Search</button>
</form><br>

<% if ((boolean) session.getAttribute("isAtt")) {
    ObjectMapper mapper = new ObjectMapper();
    java.util.Map<String, Object> map = mapper.readValue(session.getAttribute("weather").toString(), Map.class); %>
    <p>Температура: <%= map.get("temp") %></p>
    <p>Ощущается как: <%= map.get("feels_like") %></p>
    <p>Влажность: <%= map.get("humidity") + "%" %></p>
    <p>Скорость ветра: <%= map.get("wind_speed") + " м/с" %></p>
    <p>Температура воды: <%= map.get("temp_water") %></p>
<% } %>

</body>
</html>