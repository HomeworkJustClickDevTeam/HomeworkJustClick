import {useNavigate, useParams} from "react-router-dom";
import postgresqlDatabase from "../../../services/postgresDatabase";
import {AxiosError} from "axios/index";
import React, {useEffect, useState} from "react";
import Loading from "../../animations/Loading";

export default function GroupGeneralSettings(){
  const {id} = useParams<{id: string}>()
  const navigate = useNavigate()
  const [group, setGroup] = useState<{name:string, description: string, isArchived: boolean|undefined}>({
    name:"",
    description:"",
    isArchived: undefined
  })

  const groupDeletionHandler = async()=>{
    await postgresqlDatabase
      .delete(`/group/${id}`)
      .catch((error:AxiosError) => console.log(error))
    navigate("/")
  }
  const archivizationHandler = async ()=>{
    group.isArchived ?
      (await postgresqlDatabase
        .put(`/group/unarchive/${id}`)
        .catch((error:AxiosError) => console.log(error))
        .then(() => setGroup((prevState)=>({...prevState, isArchived: !prevState.isArchived}))))
      : (await postgresqlDatabase
        .put(`/group/archive/${id}`)
        .catch((error:AxiosError) => console.log(error))
        .then(() => setGroup((prevState)=>({...prevState, isArchived: !prevState.isArchived}))))
  }

  const setGroupName = async (event:React.FormEvent<HTMLFormElement>)=>{
    event.preventDefault()
    await postgresqlDatabase
      .put(`/group/name/${id}`, group.name)
      .catch((error:AxiosError) => console.log(error))
  }
  const setGroupDescription = async (event:React.FormEvent<HTMLFormElement>)=>{
    event.preventDefault()
    await postgresqlDatabase
      .put(`/group/description/${id}`, group.description)
      .catch((error:AxiosError) => console.log(error))
  }
  useEffect(() => {
    postgresqlDatabase
      .get(`/group/${id}`)
      .then((response) => {
        setGroup((prevState) => ({
          ...prevState,
          name: response.data.name,
          description: response.data.description,
          isArchived: response.data.archived}))})
      .catch((error:AxiosError) => console.log(error))
  }, [])

  if(group.isArchived === undefined)
  {
    return (<Loading/>)
  }

  return(
    <>
      <ul>
        <li>
          <form onSubmit={setGroupName}>
            <label htmlFor="groupName">
              Nazwa Grupy:
              <input type="text" name="groupName" defaultValue={group.name} onChange={
                (event)=>
                  setGroup((prevState) => (
                    {...prevState, name:event.target.value}))}/>
              <button type="submit">Potwierdź</button>
            </label>
          </form>
        </li>
        <li>
          <form onSubmit={setGroupDescription}>
            <label htmlFor="groupDescription">
              Opis Grupy:
              <input type="text" name="groupDescription" defaultValue={group.description} onChange={
                (event)=>
                  setGroup((prevState) => (
                    {...prevState, description:event.target.value}))}/>
              <button>Potwierdź</button>
            </label>
          </form>
        </li>
        <li>
          <label htmlFor="archived" className="switch">
              Zarchiwizowana:
              <input name="archived" type="checkbox" defaultChecked={group.isArchived} onClick={archivizationHandler}/>
              <span className="slider"></span>
          </label>
        </li>
        <li><button onClick={groupDeletionHandler}>Usuń grupę</button></li>
      </ul>
    </>
  )
}