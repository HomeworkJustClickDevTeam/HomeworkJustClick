import {PropsForGroupItem} from "../../../types/types";

function GroupItem ({group}:PropsForGroupItem){

    return(
    <>
        <a href={"/group/"+group.id}><h1>{group.name}</h1> </a>
        <p>{group.description}</p>
    </>
    )
}
export default GroupItem