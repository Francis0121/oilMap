<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jspf" %>

<button type="button" id="crawler">Crawler</button>

<button type="button" id="batch">Batch</button>


<p id="console"></p>

<script>
    $(function(){
        $('#crawler').on('click', function(){
            $.postJSON(contextPath+'/crawler', {}, function(result){
               $('#console').text(JSON.stringify(result));
            });
        });

        $('#batch').on('click', function(){
            $.postJSON(contextPath+'/ranking', {}, function(result){
                $('#console').text(JSON.stringify(result));
            });
        });
    });
</script>


<%@ include file="../layout/footer.jspf" %>