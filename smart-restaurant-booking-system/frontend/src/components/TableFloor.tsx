import './TableFloor.css'

interface SeatingType {
  id: number
  type: string
}

interface SeatingPreference {
  id: number
  name: string
}

interface Seating {
  id: number
  name: string
  typeId: number
  maxPeople: number
  preferenceIds: number[]
}

interface SeatingFilterResult {
  id: number
}

interface Props {
  seatings: Seating[]
  seatingTypes: SeatingType[]
  seatingPreferences: SeatingPreference[]
  filterResults: SeatingFilterResult[]
  filterResultsBooked: number[]
  onSelectSeating: (id: number) => void
}

function TableFloor({ seatings, seatingTypes, seatingPreferences, filterResults, filterResultsBooked, onSelectSeating }: Props) {

  const getSeatingPreferenceName = (preferenceId: number) => {
    return seatingPreferences.find(p => p.id === preferenceId)?.name ?? 'Unknown'
  }

  const getCardStyle = (id: number): React.CSSProperties => {
    if (filterResultsBooked.includes(id)) return { backgroundColor: '#f7c5c5' }
    if (filterResults.length === 0) return {}
    const result = filterResults.find(r => r.id === id)
    if (!result) return {}
    return { backgroundColor: '#c8f7c5'}
  }

  const groupedByType = seatingTypes.map(type => ({
    type,
    seatings: seatings.filter(s => s.typeId === type.id),
  })).filter(group => group.seatings.length > 0)

  return (
    <div className="floor-sections">
      {groupedByType.map(({ type, seatings: group }) => (
        <div key={type.id} className="floor-section">
          <h2 className="section-title">{type.type}</h2>
          <div className="floor-grid">
            {group.map(seating => (
              <div key={seating.id} className="table-card" style={{ ...getCardStyle(seating.id) }} onClick={() => onSelectSeating(seating.id)}>
                <span className="table-name">{seating.name}</span>
                <span className="table-preferences">
                  {seating.preferenceIds.map(id => getSeatingPreferenceName(id)).join(', ')}
                </span>
                <span className="table-capacity">👥 {seating.maxPeople}</span>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  )
}

export default TableFloor

