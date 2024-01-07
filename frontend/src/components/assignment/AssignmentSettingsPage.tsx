import ReactDatePicker from "react-datepicker";
import { AssignmentFile } from "./AssignmentAddOrModifyFile";
import React, { ChangeEvent, SetStateAction, useEffect, useState } from "react";
import { AssignmentModel } from "../../types/Assignment.model";
import { Table } from "../../types/Table.model";
import { AssignmentCreateModel } from "../../types/AssignmentCreate.model";
import { GroupInterface } from "../../types/GroupInterface";
import { useNavigate } from "react-router-dom";
import { setHours, setMinutes } from "date-fns";
import { FileInterface } from "../../types/FileInterface";
import { FaQuestion } from "react-icons/fa";
import { FaRegCheckCircle } from "react-icons/fa";

interface AssignmentSettingsPagePropsInterface {
  handleSubmit: (event: React.FormEvent) => void,
  assignment: AssignmentModel | AssignmentCreateModel,
  chosenEvaluationTable: number,
  handleDelete?: (event: React.FormEvent) => void,
  setChosenEvaluationTable: (tableId: number) => void,
  evaluationTable: Table[],
  setAssignment: (assignment: (prevState: any) => any) => void,
  newAssignmentId?: number,
  setNewFile: React.Dispatch<React.SetStateAction<File | undefined>>
  databaseFile?: FileInterface
}

export const AssignmentSettingsPage = ({ handleSubmit,
  assignment,
  chosenEvaluationTable,
  handleDelete,
  setChosenEvaluationTable,
  evaluationTable,
  setAssignment,
  setNewFile,
  databaseFile,
  newAssignmentId }: AssignmentSettingsPagePropsInterface) => {

  const handleTextTitleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleTextDescChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
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


  const handleTableChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    if (+event.target.value === -1)
      setChosenEvaluationTable(-1)
    else {
      setChosenEvaluationTable(+event.target.value)
      const chosenTable = evaluationTable.filter(table => table.id === +event.target.value)[0]
      setAssignment(prevState => (
        {
          ...assignment,
          maxPoints: Math.max(...chosenTable.buttons.map(button => button.points))
        })
      )
    }

  }
  const evaluationTables = (): JSX.Element[] => {
    const options = evaluationTable.map(table =>
      <option value={table.id} key={table.id}>{table.name}</option>
    )
    options.push(<option value={-1} key={-1}>Brak tabeli</option>)
    return options
  }
  return (

    <div className='flex flex-col overflow-y-hidden h-[calc(100vh-360px)] '>
      <div className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 fit pb-4 box-content overflow-y-auto">
        <div className='pl-3 border-l-2 border-main_blue lg:border-none pb-12 lg:pb-3'>
          <form onSubmit={(event) => handleSubmit(event)} className="flex flex-col gap-3 lg:flex-row">
            <div className='flex flex-col'>
              <div className='flex flex-row'>
                <label className="pr-3 w-28">
                  Tytuł:
                </label>

                <input
                  name="title"
                  type="text"
                  onChange={(event) => handleTextTitleChange(event)}
                  value={assignment.title}
                  placeholder="Nazwa zadania"
                  className="pl-1 ml-2 border-b-2 border-b-light_gray w-72"
                />
              </div>
              <div>
                <label className='flex align-top mt-2 w-24'>
                  Opis zadania:
                </label>
                <textarea
                  name="taskDescription"
                  onChange={(event) => handleTextDescChange(event)}
                  value={assignment.taskDescription}
                  placeholder="Opis zadania"
                  className="pl-1 ml-28 border border-light_gray rounded-md shadow-md w-80 min-h-[125px] mt-2 px-2 py-1"
                />
              </div>
            </div>
            <div className='flex flex-col gap-3 w-full lg:pl-5 lg:border-l-2 lg:border-main_blue'>
              <div className='flex flex-row'>
                <label className='w-60'>
                  Maksymalne punkty:  </label>
                <input
                  disabled={chosenEvaluationTable !== -1}
                  name="maxPoints"
                  type="number"
                  onChange={(event) => handleNumberChange(event)}
                  min="0"
                  value={assignment.maxPoints}
                  className="pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12"
                />
              </div>
              <div className='flex flex-row'>
                <label className='w-60'>Wybierz tabelę: </label>
                <select className="ml-2 pr-1 border-2 border-border_gray border-solid rounded-md" onChange={(event) => handleTableChange(event)} value={chosenEvaluationTable}>
                  {evaluationTables()}
                </select>
              </div>
              <div className='flex flex-row'>
                <label className='w-60'>
                  {" "}
                  Kara za wysłanie po terminie (%):</label>
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
              </div>
              <label className="flex flex-row">
                <p className="lg:w-[335px]">Data wykonania: </p>
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
              <div className='flex flex-row mt-2'>
                <label className=' mr-3'>
                  Widoczne:
                </label>
                <input
                  name="visible"
                  type="checkbox"
                  checked={assignment.visible}
                  onChange={(event) => handleCheckboxChange(event)}
                  className="mt-1 before:content[''] peer relative h-4 w-4 cursor-pointer  rounded-md border border-main_blue transition-all before:absolute before:top-2/4 before:left-2/4 before:block before:h-8 before:w-8 before:-translate-y-2/4 before:-translate-x-2/4 before:rounded-full before:bg-main_blue before:opacity-0 before:transition-opacity checked:border-main_lily checked:bg-main_blue checked:before:bg-main_lily hover:before:opacity-20"
                />
              </div>
              <div className='flex flex-row '>
                <label
                  className="mr-3 cursor-help transititext-primary text-primary transition duration-75 ease-in-out hover:text-primary-600 focus:text-primary-600 active:text-primary-700 dark:text-primary-400 dark:hover:text-primary-500 dark:focus:text-primary-500 dark:active:text-primary-600"
                  data-te-toggle='tooltip'
                  title='Włączenie tej opcji spowoduje ograniczenie rozszerzeń plików przesyłanych odpowiedzi do: .xml, .json, .txt, .png, .jpg'>
                  Ogranicz format plików:</label>
                <input
                  className="mt-1 before:content[''] peer relative h-4 w-4 cursor-pointer  rounded-md border border-main_blue transition-all before:absolute before:top-2/4 before:left-2/4 before:block before:h-8 before:w-8 before:-translate-y-2/4 before:-translate-x-2/4 before:rounded-full before:bg-main_blue before:opacity-0 before:transition-opacity checked:border-main_lily checked:bg-main_blue checked:before:bg-main_lily hover:before:opacity-20"
                  type="checkbox"
                  name='advancedEvaluation'
                  checked={assignment.advancedEvaluation}
                  onChange={(event) => handleCheckboxChange(event)} />
              </div>
            </div>
            {handleDelete !== undefined &&
              <button onClick={(event) => handleDelete(event)}
                className='absolute top-5 lg:top-3 xl:top-5 right-6 lg:right-2 xl:right-6 px-4 py-1 rounded-lg bg-berry_red text-white'>Usuń
              </button>}
            <button
              type={"submit"}
              className="absolute bottom-5 lg:bottom-3 xl:bottom-5 right-6 lg:right-2 xl:right-6 px-8 py-1 rounded-lg bg-main_blue text-white hover:bg-hover_blue hover:shadow-md active:shadow-none"
            >Zapisz </button>
          </form>
          <p className="mt-4 mb-2">Plik do zadania: </p>
          {(assignment as AssignmentModel).id !== undefined && newAssignmentId === undefined ?
            <AssignmentFile
              setNewFile={setNewFile}
              assignmentId={(assignment as AssignmentModel).id}
              databaseFile={databaseFile}
            /> :
            <AssignmentFile
              setNewFile={setNewFile}
              assignmentId={newAssignmentId!}
            />}
        </div>
      </div>
    </div>

  )
}