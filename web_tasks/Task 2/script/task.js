class Task{
    constructor(ID, Title){
        this.id = ID
        this.title = Title;
        this.isCompleted = false;
    }

    setCompleted(status) {
        this.isCompleted = status;
    }

}