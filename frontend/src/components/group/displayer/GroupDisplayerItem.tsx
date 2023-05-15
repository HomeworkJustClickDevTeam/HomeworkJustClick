import {PropsForGroupItem} from "../../../types/types";

function GroupDisplayerItem ({group}:PropsForGroupItem){

    return(
    <>
        <a href={"/group/fullDescription/"+group.id}><h1>{group.name}</h1> </a>
        <p>{group.description}</p>
    </>
    )
}
export default GroupDisplayerItem