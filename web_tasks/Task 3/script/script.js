$(document).ready(function () {

    $("button").on("click", function () {
        var currentKey = $(this).data("value");

        if (currentKey == "AC") {
            $("#diaplay").val("0");
            return;
        } else if (currentKey == "DEL") {
            $("#diaplay").val(function (index, value) {
                return this.value.substr(0, value.length - 1);
            });

            if ($("#diaplay").val() == "") {
                $("#diaplay").val("0");
            }

            return;
        } else if (currentKey == "=") {
            $("#diaplay").val(calculate($("#diaplay").val()));
            return;
        }

        if ($("#diaplay").val() == "0") {
            $("#diaplay").val("");
        }

        $("#diaplay").val(function () {
            return this.value + currentKey;
        });
    });

});

function calculate(exp) {
    return new Calculator().evaluate(exp)
}
