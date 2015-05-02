<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jspf" %>

<button type="button" id="crawler">Crawler</button>

<script>
    $(function(){
        $('#crawler').on('click', function(){
            $.postJSON(contextPath+'/crawler', {}, function(result){
               console.log(result);
            });
        });
    });
</script>


<%@ include file="../layout/footer.jspf" %>