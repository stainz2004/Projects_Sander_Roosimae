import api from '../services/api'
import { useState, useEffect } from 'react'
import './Filtering.css'

interface SeatingType {
    id: number
    type: string
}

interface SeatingPreference {
    id: number,
    name: string
}

interface SeatingFilterResult {
    id: number
}

interface FilteringProps {
    seatingTypes: SeatingType[]
    seatingPreferences: SeatingPreference[]
    onFilterResults: (results: SeatingFilterResult[]) => void
    onFilterBookedResults: (results : number[]) => void
    onDateAndTimeChange?: (value: string) => void
    refreshKey: number
}

function Filtering({ seatingTypes, seatingPreferences, onFilterResults, onFilterBookedResults, onDateAndTimeChange, refreshKey }: FilteringProps) {
    const [dateAndTime, setDateAndTime] = useState<string>('')

    const handleDateAndTimeChange = (value: string) => {
        setDateAndTime(value)
        onDateAndTimeChange?.(value)
    }
    const [numberOfPeople, setNumberOfPeople] = useState<number>(1)
    const [seatingTypeId, setSeatingTypeId] = useState<number | ''>('')
    const [selectedPreference, setSelectedPreference] = useState<number | ''>('')
    const [lastAction, setLastAction] = useState<'filter' | 'suggest' | null>(null)

    useEffect(() => {
        if (!dateAndTime) return

        const formattedDate = `${dateAndTime}:00`

        api.get<number[]>('/seatings/booked', {
            params: {
                dateAndTime: formattedDate
            }
        })
            .then(response => onFilterBookedResults(response.data))
            .catch(error => alert(error.response?.data?.message ?? error.message))

    }, [dateAndTime, refreshKey])

    useEffect(() => {
        if (!lastAction || !dateAndTime) return
        if (lastAction === 'filter') handleFilter()
        if (lastAction === 'suggest') handleSuggest()
    }, [refreshKey])

    const handleSuggest = () => {
        if (!dateAndTime || !numberOfPeople) {
            alert('Date & Time and Number of People are required.')
            return
        }

        const formattedDate = dateAndTime ? `${dateAndTime}:00` : undefined

        api.get<SeatingFilterResult[]>('/seatings/suggestions', {
            params: {
                ...(formattedDate && {dateAndTime: formattedDate}),
                numberOfPeople,
                ...(seatingTypeId !== '' && {seatingTypeId}),
                ...(selectedPreference !== '' && {selectedPreference})
            }
        })
            .then(response => {
                onFilterResults(response.data)
                setLastAction('suggest')
            })
            .catch(error => alert(error.response?.data?.message ?? error.message))
    }

    const handleFilter = () => {
        const formattedDate = dateAndTime ? `${dateAndTime}:00` : undefined

        api.get<SeatingFilterResult[]>('/seatings/filter', {
            params: {
                ...(formattedDate && {dateAndTime: formattedDate}),
                numberOfPeople,
                ...(seatingTypeId !== '' && {seatingTypeId}),
                ...(selectedPreference !== '' && {selectedPreference})
            }
        })
            .then(response => {
                onFilterResults(response.data)
                setLastAction('filter')
            })
            .catch(error => alert(error.response?.data?.message ?? error.message))

    }

    return (
        <div className="filtering-bar">
            <div className="filter-field">
                <label>Aeg</label>
                <input
                    type="datetime-local"
                    value={dateAndTime}
                    onChange={e => handleDateAndTimeChange(e.target.value)}
                />
            </div>

            <div className="filter-field">
                <label>Inimeste arv</label>
                <input
                    type="number"
                    min={1}
                    value={numberOfPeople}
                    onChange={e => setNumberOfPeople(Number(e.target.value))}
                />
            </div>

            <div className="filter-field">
                <label>Tsoon</label>
                <select
                    value={seatingTypeId}
                    onChange={e => setSeatingTypeId(e.target.value === '' ? '' : Number(e.target.value))}
                >
                    <option value="">Any</option>
                    {seatingTypes.map(st => (
                        <option key={st.id} value={st.id}>{st.type}</option>
                    ))}
                </select>
            </div>

            <div className="filter-field">
                <label>Eelistus</label>
                <select
                    value={selectedPreference}
                    onChange={e => setSelectedPreference(e.target.value === '' ? '' : Number(e.target.value))}
                >
                    <option value="">Any</option>
                    {seatingPreferences.map(sp => (
                        <option key={sp.id} value={sp.id}>{sp.name}</option>
                    ))}
                </select>
            </div>

            <div className="filter-buttons">
                <button className="btn-filter" onClick={handleFilter}>Filtreeri</button>
                <button className="btn-suggest" onClick={handleSuggest}>Soovita laud</button>
            </div>
        </div>
    )
}

export default Filtering
