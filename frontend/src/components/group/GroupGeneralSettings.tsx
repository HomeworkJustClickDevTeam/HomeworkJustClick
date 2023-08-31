import {useNavigate, useParams} from "react-router-dom"
import {
  deleteGroupPostgresService,
  getGroupPostgresService,
  putGroupArchivePostgresService,
  putGroupDescriptionPostgresService,
  putGroupNamePostgresService,
  putGroupUnarchivePostgresService
} from "../../services/postgresDatabaseServices"
import {AxiosError} from "axios"
import React, {useContext, useEffect, useState} from "react"
import Loading from "../animations/Loading"
import ApplicationStateContext from "../../contexts/ApplicationStateContext";

export default function GroupGeneralSettings() {
  const {applicationState, setApplicationState} = useContext(ApplicationStateContext)
  const navigate = useNavigate()
  const currentURL = `${window.location.origin}/group/${applicationState?.group?.id}`

  const groupDeletionHandler = async () => {
    await deleteGroupPostgresService(applicationState?.group?.id as unknown as string)
      .catch((error: AxiosError) => console.log(error))
    setApplicationState({type:"setGroupView", group:undefined})
    navigate("/")
  }
  const archivizationHandler = async () => {
    applicationState?.group?.archived
      ? await putGroupUnarchivePostgresService(applicationState?.group?.id as unknown as string)
        .catch((error: AxiosError) => console.log(error))
        .then(() =>{
          let group = applicationState?.group
          if(group !== undefined){
            group.archived = !group?.archived
            setApplicationState({type:"setGroupView", group: group})
          }
        })
      : await putGroupArchivePostgresService(applicationState?.group?.id as unknown as string)
        .catch((error: AxiosError) => console.log(error))
        .then(() =>{
          let group = applicationState?.group
          if(group !== undefined){
            group.archived = !group?.archived
            setApplicationState({type:"setGroupView", group: group})
          }
        })
  }

  const setGroupName = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if(applicationState?.group !== undefined) {
      await putGroupNamePostgresService(applicationState?.group?.id as unknown as string, applicationState?.group?.name)
        .catch((error: AxiosError) => console.log(error))
    }
  }
  const setGroupDescription = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault()
    if(applicationState?.group !== undefined) {
      await putGroupDescriptionPostgresService(applicationState?.group?.id as unknown as string, applicationState?.group?.description)
        .catch((error: AxiosError) => console.log(error))
    }
  }
  const handleCopyUrl = async () => {
    try {
      await navigator.clipboard.writeText(currentURL)
    } catch (error) {
      console.log(error)
    }
  }
  useEffect(() => {
    getGroupPostgresService(applicationState?.group?.id as unknown as string)
      .then((response) => {
        setApplicationState({type: "setGroupView", group: response.data})
      })
      .catch((error: AxiosError) => console.log(error))
  }, [])

  if (applicationState?.group?.archived === undefined) {
    return <Loading/>
  }

  return (
    <>
      <ul>
        <li>
          <form onSubmit={setGroupName}>
            <label htmlFor="groupName">
              Nazwa Grupy:
              <input
                type="text"
                name="groupName"
                defaultValue={applicationState?.group?.name}
                onChange={(event) =>{
                  let group = applicationState?.group
                  if(group !== undefined){
                    group.name = event.target.value
                    setApplicationState({type:"setGroupView", group: group})
                  }}
                }
                className='border-b-2 border-b-light_gray mx-2 mr-8'
              />
              <button type="submit" className='bg-main_blue px-4 text-white rounded-md'>Potwierdź</button>
            </label>
          </form>
        </li>
        <li>
          <form onSubmit={setGroupDescription}>
            <label htmlFor="groupDescription">
              Opis Grupy:
              <input
                type="text"
                name="groupDescription"
                defaultValue={applicationState?.group?.description}
                onChange={(event) =>{
                    let group = applicationState?.group
                    if(group !== undefined){
                      group.name = event.target.value
                      setApplicationState({type:"setGroupView", group: group})
                    }
                }
               }
                className='border-b-2 border-b-light_gray mx-2 mr-8'
              />
              <button className='bg-main_blue px-4 text-white rounded-md'>Potwierdź</button>
            </label>
          </form>
        </li>
        <li>
          <label htmlFor="archived" className="switch">
            Zarchiwizowana:
            <input
              className='ml-2 cursor-pointer'
              name="archived"
              type="checkbox"
              defaultChecked={applicationState?.group?.archived}
              onClick={archivizationHandler}
            />
            <span className="slider"></span>
          </label>
        </li>
        <li>
          <button onClick={groupDeletionHandler} className='text-scarlet '>Usuń grupę</button>
        </li>
        <li>
          <button onClick={handleCopyUrl} className='underline'>Udostepnij Link grupy</button>
        </li>
      </ul>
    </>
  )
}
