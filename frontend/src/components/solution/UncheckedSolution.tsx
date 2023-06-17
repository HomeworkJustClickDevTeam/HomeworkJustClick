import {Assigment, Solution} from "../../types/types";
import {useEffect, useState} from "react";
import {Evaluation} from "../../types/Evaluation";
import postgresqlDatabase from "../../services/postgresDatabase";
import {AxiosError} from "axios";
import Loading from "../animations/Loading";

export default function UncheckedSolution(props:{solution:Solution, assignment:Assigment}){
  return(
    <>
      Zadanie: {props.assignment.title} <br/>
      Opis zadania: {props.assignment.taskDescription}<br/>
      Wynik: ?/{props.assignment.max_points}
    </>
  )
}