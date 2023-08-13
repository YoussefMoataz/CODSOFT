let storage = window.localStorage

var currentTaskID = -1;

$(document).ready(function () {

    let tasksListJSON = storage.getItem("tasks_list");
    let tasksList;

    if (tasksListJSON == null) {
        tasksList = [];
    } else {
        tasksList = JSON.parse(tasksListJSON);
    }

    refreshList(tasksList);

    $("#btnSaveTask").on("click", function () {

        if ($("#inputTask").val() == "") {
            alert("Task can't be empty!");
        } else {

            if (findTask(tasksList, currentTaskID) == -1) {
                // add task

                tasksList[tasksList.length] = new Task(tasksList.length, $("#inputTask").val());
                storage.setItem("tasks_list", JSON.stringify(tasksList));
                refreshList(tasksList);
            } else {
                // update task

                tasksList[currentTaskID].title = $("#inputTask").val();
                storage.setItem("tasks_list", JSON.stringify(tasksList));
                refreshList(tasksList);
            }

            $("#inputTask").val("");
        }

        currentTaskID = -1;

    });

    $("#clearTask").on("click", function () {
        $("#inputTask").val("");
        currentTaskID = -1;
    });

});

function refreshList(tasksList) {
    let new_ul = [];
    tasksList.forEach(task => {

        var isChecked = "";
        if (task.isCompleted) {
            isChecked = "checked";
        }

        new_ul.push("<li id=\"" + task.id + "\">"
            + "<input type=\"checkbox\" class=\"" + task.id + "\"" + isChecked + ">"
            + task.title
            + "<div><input type=\"button\" value=\"Delete\" class=\"deleteButton " + task.id + "\"></div>"
            + "</li>"
            + "<hr>");
    })

    $('.items-container').html("<ul class=\"items-list\">" + new_ul.join("") + "</ul>")

    $(".items-list").on("click", "li", function () {
        let id = $(this).attr('id');
        currentTaskID = id;
        $("#inputTask").val(tasksList[currentTaskID].title);
    });

    $("input[type=\"checkbox\"]").change(function () {
        let id = $(this).attr('class');
        currentTaskID = parseInt(id);
        tasksList[currentTaskID].isCompleted = ($(this).is(":checked"));
        storage.setItem("tasks_list", JSON.stringify(tasksList));
    });

    $(".deleteButton").on("click", function () {
        let id = $(this).attr('class').split(" ")[1];
        currentTaskID = parseInt(id);

        if (confirm("Are you sure you want to delete this task ?")) {
            tasksList.splice(currentTaskID, 1);

            for (i = 0; i < tasksList.length; i++) {
                tasksList[i].id = i;

            }

            storage.setItem("tasks_list", JSON.stringify(tasksList));
            $("#inputTask").val("");
            currentTaskID = -1;
            refreshList(tasksList);
        }
    });

}

function findTask(tasksList, taskID) {

    for (i = 0; i < tasksList.length; i++) {

        if (tasksList[i].id == taskID) {
            return i;
        }

    }

    return -1;

}