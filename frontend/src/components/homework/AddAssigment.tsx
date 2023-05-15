function AddAssigment(){
    return(
        <>
            <form onSubmit={() =>{console.log("DATA")}}>
                <input name="title" type="text"/>
                <input name="description" type="text"/>
                <input name="visible" type="checkbox"/>
                <button>
                    Dodaj zadanie domowe
                </button>
            </form>
        </>

    )

}
export default AddAssigment