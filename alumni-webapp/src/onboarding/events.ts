
export type State = 'unexpected_error' | 'office_signin' | 'home_signin' | 'signed_with_invalid_token' | 'verification_required' |
  'assign_national_id' | 'national_id_assignment_verification' | 'assign_national_id_failed' | 'loading' |
  'onboarded_nodata' | 'onboarded_service_unavailable' | 'onboarded'

export const MSAL_READY = 'MSAL_READY';
