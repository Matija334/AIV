import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import api from "../../services/api";

export default function PatientDetails() {
    const navigate = useNavigate();
    const {patientEmail} = useParams();
    const [patient, setPatient] = useState<any>([]);
    const [doctors, setDoctors] = useState<any>([]);
    const [docEmail, setDocEmail] = useState<string>("");

    const handleSubmit = async (event: { preventDefault: () => void; }) => {
        event.preventDefault();
        console.log(docEmail);
        try {
            const result = await api.put(`/patients/${patient.email}/${docEmail}`);
        } catch (error) {
            console.log(error);
        }
        window.location.reload();
    };

    useEffect(() => {
        const getPatient = async () => {
            try {
                const result = await api.get(`/patients/${patientEmail}`);
                setPatient(result.data);
                console.log(result.data);
            } catch (error) {
                console.log(error);
            }
        };

        const getDoctors = async () => {
            try {
                const result = await api.get("/doctors");
                setDoctors(result.data);
                console.log(result.data);
            } catch (error) {
                console.log(error);
            }
        };

        getPatient();
        getDoctors();
    }, []);
    return (
        <>
            <h1>Patient Details</h1>
            <div>
                <span>Name: </span><span>{patient.name}</span><br />
                <span>Last name: </span><span>{patient.lastName}</span><br />
                <span>Email: </span><span>{patient.email}</span><br />
                <span>Birthday: </span><span>{patient.birthday}</span><br />
                <span>Info: </span><span>{patient.info}</span><br />
                <span>Personal doctor: </span><span>{patient.name_doctor} {patient.lastName_doctor}</span><br />
                <form onSubmit={handleSubmit}>
                    <label>Select personal doctor: </label>
                    <select value={docEmail} onChange={(event) => setDocEmail(event.target.value)}>
                        <option value="">Select doctor...</option>
                        {doctors.map((option: any, index: number) => (
                            <option key={index} value={option.email}>{option.name} {option.lastName}</option>
                        ))}
                    </select> <br />
                    <button type="submit">
                        Submit
                    </button>
                </form>
            </div>
        </>
    );
}