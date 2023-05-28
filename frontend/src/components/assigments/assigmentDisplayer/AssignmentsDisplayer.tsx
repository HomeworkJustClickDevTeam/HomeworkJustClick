import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Assigment} from "../../../types/types";
import common_request from "../../../services/default-request-database";
import AssigmentItem from "./assigmentItem/AssigmentItem";

function AssignmentsDisplayer() {
    const [assigments,setAssigments] = useState<Assigment[]>([])
    const [assigmentsVisible,setVisibleAssigments] = useState<Assigment[]>([])
    const {id=""} = useParams<string>()

    useEffect(() =>{common_request.get("/assignments/byGroupId/"+id).then( response => {
                const assigmentFromServer = response.data as Assigment []
                setAssigments(assigmentFromServer)
                setVisibleAssigments(assigmentFromServer.filter(assigment => assigment.visible))
            }
        )
        },
        [])


    return<>
        <Link to={`/group/${id}/assignments/add`}>
            <button>
                Dodaj zadanie domowe
            </button>
        </Link>
        <ul>
            {assigmentsVisible.map((assigment) =>
                <li key={assigment.id}><AssigmentItem
                                   assigment={assigment} idGroup={id}/> </li>)}

        </ul>
    </>
}
export default AssignmentsDisplayer
