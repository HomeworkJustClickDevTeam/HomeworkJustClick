import ReactDatePicker from "react-datepicker";
import {AssignmentFile} from "./AssignmentAddOrModifyFile";
import React, {ChangeEvent, SetStateAction, useEffect, useState} from "react";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {Table} from "../../types/Table.model";
import {AssignmentToSendInterface} from "../../types/AssignmentToSendInterface";
import {GroupInterface} from "../../types/GroupInterface";
import {useNavigate} from "react-router-dom";
import {setHours, setMinutes} from "date-fns";
import {FileInterface} from "../../types/FileInterface";

interface AssignmentSettingsPagePropsInterface{
  handleSubmit: (event: React.FormEvent) => void,
  assignment: AssignmentInterface|AssignmentToSendInterface,
  chosenEvaluationTable:number,
  handleDelete?:(event: React.FormEvent) => void,
  setChosenEvaluationTable:(tableId: number)=>void,
  evaluationTable:Table[],
  setAssignment: (assignment: (prevState: any) => any) => void,
  newAssignmentId?: number,
  setNewFile: React.Dispatch<React.SetStateAction<File|undefined>>
  databaseFile?: FileInterface
}

export const AssignmentSettingsPage = ({handleSubmit,
                                         assignment,
                                         chosenEvaluationTable,
                                         handleDelete,
                                         setChosenEvaluationTable,
                                         evaluationTable,
                                         setAssignment,
                                         setNewFile,
                                         databaseFile,
                                         newAssignmentId}:AssignmentSettingsPagePropsInterface) =>{

  const handleTextChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }
  const handleNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleCheckboxChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: checked,
    }))
  }

  const handleDateChange = (date: Date) => {
    setAssignment((prevState) => ({
      ...prevState,
      completionDatetime: date,
    }))
  }


  const handleTableChange = (event:React.ChangeEvent<HTMLSelectElement>) => {
    if(+event.target.value === -1)
      setChosenEvaluationTable(-1)
    else
    {
      setChosenEvaluationTable(+event.target.value)
      const chosenTable = evaluationTable.filter(table => table.id === +event.target.value)[0]
      setAssignment(prevState => (
        {...assignment,
          maxPoints: Math.max(...chosenTable.buttons.map(button => button.points))})
      )
    }

  }
  const evaluationTables = ():JSX.Element[] => {
    const options = evaluationTable.map(table =>
      <option value={table.id} key={table.id}>{table.name}</option>
    )
    options.push(<option value={-1} key={-1}>Brak tabeli</option>)
    return options
  }
  return (

      <div className='overflow-y-hidden'>
        <div className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 fit pb-4 box-content overflow-y-auto">
          <form onSubmit={(event) => handleSubmit(event)} className="flex flex-col gap-3">
            <label className="pr-3">
              Tytuł
              <input
                name="title"
                type="text"
                onChange={(event)=>handleTextChange(event)}
                value={assignment.title}
                placeholder="Nazwa zadania"
                className="pl-1 ml-2 border-b-2 border-b-light_gray w-64"
              />
            </label>

            <label>
              Opis zadania
              <input
                name="taskDescription"
                type="text"
                onChange={(event)=>handleTextChange(event)}
                value={assignment.taskDescription}
                placeholder="Opis zadania"
                className="pl-1 ml-2 border-b-2 border-b-light_gray w-80"
              />
            </label>
            <label>
              Maksymalne punkty
              <input
                disabled={chosenEvaluationTable !== -1}
                name="maxPoints"
                type="number"
                onChange={(event)=>handleNumberChange(event)}
                min="0"
                value={assignment.maxPoints}
                className="pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12"
              />
            </label>
            <label>Wybierz tabelę:
              <select className="ml-2 pr-1 border-2 border-border_gray border-solid rounded-md" onChange={(event) => handleTableChange(event)} value={chosenEvaluationTable}>
                {evaluationTables()}
              </select>
            </label>
            <label>
              {" "}
              Kara za wysłanie po terminie (%)
              <input
                name="autoPenalty"
                type="number"
                onChange={handleNumberChange}
                min="0"
                max="100"
                step="25"
                value={assignment.autoPenalty}
                className="pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12"
              />
            </label>
            <label className="flex">
              <p className="w-36">Data wykonania: </p>
              <ReactDatePicker
                name="completionDatetime"
                selected={new Date(assignment.completionDatetime)}
                onChange={handleDateChange}
                showTimeSelect
                timeFormat="HH:mm"
                minDate={new Date()}
                minTime={new Date()}
                maxTime={setHours(setMinutes(new Date(), 59), 23)}
                timeIntervals={15}
                dateFormat="yyyy-MM-dd HH:mm"
                className="pl-1 ml-2 border-b-2 border-b-light_gray w-36 cursor-pointer"
              />
            </label>
            <label>
              Widoczne:
              <input
                name="visible"
                type="checkbox"
                checked={assignment.visible}
                onChange={(event)=>handleCheckboxChange(event)}
              />
            </label>
            <button
              type={"submit"}
              className="absolute bottom-5 right-0 mr-6 mt-4 px-8 py-1 rounded-lg bg-main_blue text-white hover:bg-hover_blue hover:shadow-md active:shadow-none"
            >
              Zapisz
            </button>
          </form>
          <p className="mt-4 mb-2">Nowy plik: </p>
          {(assignment as AssignmentInterface).id !== undefined && newAssignmentId === undefined ?
          <AssignmentFile
            setNewFile={setNewFile}
            assignmentId={(assignment as AssignmentInterface).id}
            databaseFile={databaseFile}
          />:
            <AssignmentFile
              setNewFile={setNewFile}
              assignmentId={newAssignmentId!}
            />}
          {handleDelete !== undefined &&
          <button onClick={(event) => handleDelete(event)}
                  className='absolute top-5 right-0 mr-6 mb-4 px-4 py-1 rounded-lg bg-berry_red text-white'>Usuń
          </button>}
        </div>
      </div>

  )
}