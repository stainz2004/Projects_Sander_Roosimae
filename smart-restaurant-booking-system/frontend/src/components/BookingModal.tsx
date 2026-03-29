interface BookingModalProps {
  selectedSeatingId: number;
  bookingTime: string;
  onConfirm: () => void;
  onCancel: () => void;
}

function BookingModal({ selectedSeatingId, bookingTime, onConfirm, onCancel }: BookingModalProps) {
  return (
    <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
      <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0,0,0,0.2)', minWidth: '300px' }}>
        <h3 style={{ marginTop: 0 }}>Book Table #{selectedSeatingId}</h3>
        <p style={{ margin: '0 0 4px', color: '#555' }}>Date &amp; time:</p>
        <p style={{ margin: '0 0 8px', fontWeight: 500 }}>
          {bookingTime
            ? bookingTime.replace('T', ' ')
            : <em style={{ color: '#888' }}>No date selected — use the filter above</em>}
        </p>
        <div style={{ marginTop: '16px', display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
          <button
            onClick={onCancel}
            style={{ padding: '8px 16px', borderRadius: '6px', border: '1px solid #ccc', background: '#f5f5f5', cursor: 'pointer', fontWeight: 500 }}
          >Cancel</button>
          <button
            onClick={onConfirm}
            disabled={!bookingTime}
            style={{ padding: '8px 16px', borderRadius: '6px', border: 'none', background: bookingTime ? '#2e7d32' : '#a5d6a7', color: 'white', cursor: bookingTime ? 'pointer' : 'not-allowed', fontWeight: 500 }}
          >Confirm</button>
        </div>
      </div>
    </div>
  );
}

export default BookingModal;
