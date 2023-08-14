$(document).ready(function () {

    $(".nav-list").on("click", "li", function () {

        $(".nav-list").children().children(".active").removeAttr("class");
        $(this).children().attr("class", "active");
    });

});
