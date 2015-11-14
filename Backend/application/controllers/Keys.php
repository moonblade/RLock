<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Keys extends CI_Controller {

	/**
	 * Index Page for this controller.
	 *
	 * Maps to the following URL
	 * 		http://example.com/index.php/welcome
	 *	- or -
	 * 		http://example.com/index.php/welcome/index
	 *	- or -
	 * Since this controller is set as the default controller in
	 * config/routes.php, it's displayed at http://example.com/
	 *
	 * So any other public methods not prefixed with an underscore will
	 * map to /index.php/welcome/<method_name>
	 * @see http://codeigniter.com/user_guide/general/urls.html
	 */

	public function index()
	{
		print "keys";
	}

	public function viewCurrent()
	{
		$result['status']=0;
		$result['message']="Failed";
		$temp['id'] = $this->input->post('id');
		if($this->db->get_where('users',$temp)->row_array()['level']>0)
		{
			$query = $this->db->query("select passkey, date, name from passkeys natural join users where kid = (select max(kid) from passkeys)");
			$row = $query->row_array();
			if($row)
			{
				$result['status']=1;
				$result['message']=$row;
			}
		}
		else
		{
			$result['status']=0;
			$result['message']="Insufficient permission";
		}
		print json_encode($result);
	}
}
