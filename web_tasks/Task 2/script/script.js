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

                tasksList[currentTaskID] = new Task(currentTaskID, $("#inputTask").val());
                storage.setItem("tasks_list", JSON.stringify(tasksList));
                refreshList(tasksList);
            }
        }

    });

});

function refreshList(tasksList) {
    let new_ul = [];
    tasksList.forEach(task => {
        new_ul.push("<li id=\"" + task.id + "\">" + task.title + " - " + task.isCompleted + "</li>");
    })

    $('.items-container').html("<ul class=\"items-list\">" + new_ul.join("") + "</ul>")

    $(".items-list").on("click", "li", function () {
        let id = $(this).attr('id');
        currentTaskID = id;
        $("#inputTask").val(tasksList[currentTaskID].title);
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