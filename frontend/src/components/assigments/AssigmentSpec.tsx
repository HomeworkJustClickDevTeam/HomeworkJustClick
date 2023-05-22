import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import common_request from "../../services/default-request-database";
import {Assigment} from "../../types/types";
import AddSolution from "../solution/AddSolution";

function AssigmentSpec(){
    const {id,idAssigment}= useParams()
    const [assigment,setAssigment] = useState<Assigment>()
    useEffect(()=>{
        common_request.get(`/assignment/${idAssigment}`).then(respone =>
        setAssigment(respone.data))
    },[])
    return(
        <>
            <h1>
                {assigment?.title}
            </h1>
            <p>
                {assigment?.taskDescription}
            </p>
            <h2>{assigment?.completionDatetime.toLocaleString()}</h2>
            <AddSolution/>
        </>
    )
}
export default AssigmentSpec